from sqlalchemy import create_engine, Column, Integer, String, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session, relationship

DATABASE_URL = "mysql+mysqlconnector://root:123456@47.100.125.229/xx_test"

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(50), unique=True, index=True)
    password = Column(String(50))

class Item(Base):
    __tablename__ = "items"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(50))
    user_id = Column(Integer, ForeignKey("users.id"))
    user = relationship("User", back_populates="items")

User.items = relationship("Item", back_populates="user")

def create_tables():
    print("尝试在数据库中创建表格。")
    Base.metadata.create_all(bind=engine)
    print("成功创建表格。")

# Other database-related functions can be added here.

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
