import os
import json
from deepdiff import DeepDiff

def compare_two_json_files(file1_path, file2_path):
    """Compare a single pair of JSON files and return detailed differences."""
    try:
        # Read and parse JSON files
        with open(file1_path, 'r', encoding='utf-8') as f1:
            data1 = json.load(f1)
        with open(file2_path, 'r', encoding='utf-8') as f2:
            data2 = json.load(f2)
        
        # Perform deep diff comparison
        diff = DeepDiff(data1, data2, ignore_order=True)  # Ignore list order differences
        if not diff:
            return "✅ 无差异"
        else:
            # Format diff output and label difference types
            result = "❌ 存在差异：\n"
            if "dictionary_item_added" in diff:
                result += f"  - 新增键：{diff['dictionary_item_added']}\n"
            if "dictionary_item_removed" in diff:
                result += f"  - 缺失键：{diff['dictionary_item_removed']}\n"
            if "values_changed" in diff:
                result += f"  - 值不匹配：{diff['values_changed']}\n"
            return result
    except json.JSONDecodeError:
        return "❌ 错误：JSON格式无效，无法解析"
    except FileNotFoundError:
        return "❌ 错误：文件不存在"
    except Exception as e:
        return f"❌ 未知错误：{str(e)}"

def batch_compare_json_folders(folder1, folder2, report_file="json_diff_report.txt"):
    """Batch-compare same-named JSON files in two folders and generate a report."""
    # Collect JSON filenames from both folders
    json_files1 = [f for f in os.listdir(folder1) if f.endswith('.json')]
    json_files2 = [f for f in os.listdir(folder2) if f.endswith('.json')]
    
    # Identify shared and unique files
    common_files = set(json_files1) & set(json_files2)
    unique_files1 = set(json_files1) - set(json_files2)
    unique_files2 = set(json_files2) - set(json_files1)
    
    # Build comparison report
    report = []
    report.append(f"📊 JSON文件批量对比报告（{folder1} vs {folder2}）\n")
    
    # Handle files that exist in only one folder
    if unique_files1:
        report.append(f"⚠️ 仅在{folder1}存在的JSON文件：\n  {', '.join(unique_files1)}\n")
    if unique_files2:
        report.append(f"⚠️ 仅在{folder2}存在的JSON文件：\n  {', '.join(unique_files2)}\n")
    
    # Compare differences for files with the same name
    report.append(f"🔍 同名文件差异详情（共{len(common_files)}个）：\n")
    for file_name in sorted(common_files):
        file1_path = os.path.join(folder1, file_name)
        file2_path = os.path.join(folder2, file_name)
        diff_result = compare_two_json_files(file1_path, file2_path)
        report.append(f"  {file_name}：\n    {diff_result}\n")
    
    # Save report to file and print it
    final_report = "\n".join(report)
    with open(report_file, 'w', encoding='utf-8') as f:
        f.write(final_report)
    print(final_report)
    print(f"\n📄 报告已保存到：{os.path.abspath(report_file)}")

# ------------------------- Please update folder paths below ------------------------
FOLDER_PATH1 = r"C:\yourfistdir"  # Example: r"C:\data\old"
FOLDER_PATH2 = r"C:\yourseconddir"  # Example: r"C:\data\new"
# -------------------------------------------------------------------------------

if __name__ == "__main__":
    batch_compare_json_folders(FOLDER_PATH1, FOLDER_PATH2)