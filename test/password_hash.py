from passlib.context import CryptContext

# 创建密码上下文
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# 原始密码
original_password = "123456"

# 哈希密码
hashed_password = pwd_context.hash(original_password)

print("Original Password:", original_password)
print("Hashed Password:", hashed_password)
