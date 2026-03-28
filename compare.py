import os
import json
from deepdiff import DeepDiff

def compare_two_json_files(file1_path, file2_path):
    """对比单个JSON文件的差异，返回差异详情"""
    try:
        # 读取并解析JSON文件
        with open(file1_path, 'r', encoding='utf-8') as f1:
            data1 = json.load(f1)
        with open(file2_path, 'r', encoding='utf-8') as f2:
            data2 = json.load(f2)
        
        # 深度对比差异
        diff = DeepDiff(data1, data2, ignore_order=True)  # 忽略列表顺序差异
        if not diff:
            return "✅ 无差异"
        else:
            # 格式化差异结果，标注差异类型
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
    """批量对比两个文件夹下的同名JSON文件，生成报告"""
    # 获取两个文件夹中的JSON文件名
    json_files1 = [f for f in os.listdir(folder1) if f.endswith('.json')]
    json_files2 = [f for f in os.listdir(folder2) if f.endswith('.json')]
    
    # 找到同名文件
    common_files = set(json_files1) & set(json_files2)
    unique_files1 = set(json_files1) - set(json_files2)
    unique_files2 = set(json_files2) - set(json_files1)
    
    # 生成对比报告
    report = []
    report.append(f"📊 JSON文件批量对比报告（{folder1} vs {folder2}）\n")
    
    # 处理仅在一个文件夹中存在的文件
    if unique_files1:
        report.append(f"⚠️ 仅在{folder1}存在的JSON文件：\n  {', '.join(unique_files1)}\n")
    if unique_files2:
        report.append(f"⚠️ 仅在{folder2}存在的JSON文件：\n  {', '.join(unique_files2)}\n")
    
    # 处理同名文件的差异
    report.append(f"🔍 同名文件差异详情（共{len(common_files)}个）：\n")
    for file_name in sorted(common_files):
        file1_path = os.path.join(folder1, file_name)
        file2_path = os.path.join(folder2, file_name)
        diff_result = compare_two_json_files(file1_path, file2_path)
        report.append(f"  {file_name}：\n    {diff_result}\n")
    
    # 保存报告到文件并打印
    final_report = "\n".join(report)
    with open(report_file, 'w', encoding='utf-8') as f:
        f.write(final_report)
    print(final_report)
    print(f"\n📄 报告已保存到：{os.path.abspath(report_file)}")

# -------------------------- 请修改这里的文件夹路径 --------------------------
FOLDER_PATH1 = r"C:\yourfistdir"  # 例：r"C:\data\old"
FOLDER_PATH2 = r"C:\yourseconddir"  # 例：r"C:\data\new"
# --------------------------------------------------------------------------

if __name__ == "__main__":
    batch_compare_json_folders(FOLDER_PATH1, FOLDER_PATH2)