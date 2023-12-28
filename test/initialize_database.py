# initialize_database.py
import sqlite3
from pathlib import Path

def execute_sql_file(db_path, sql_file_path):
    with sqlite3.connect(db_path) as conn:
        cursor = conn.cursor()
        with open(sql_file_path, 'r') as sql_file:
            sql_script = sql_file.read()
            cursor.executescript(sql_script)
        conn.commit()

def initialize_database(db_path, sql_file_path):
    if not Path(db_path).exists():
        print("Database file not found. Creating a new database.")
        execute_sql_file(db_path, sql_file_path)
        print("Database initialized successfully.")
    else:
        print("Database file found. Reading existing database.")

if __name__ == "__main__":
    # 替换为实际的数据库文件路径和 SQL 文件路径
    db_path = "test.db"
    sql_file_path = "create_tables.sql"
    
    # 获取绝对路径
    db_path = Path(db_path).resolve()
    sql_file_path = Path(sql_file_path).resolve()
    print(db_path)
    print(sql_file_path)

    initialize_database(db_path, sql_file_path)
