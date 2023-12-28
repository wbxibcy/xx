from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy import create_engine, Column, Integer, String, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session, relationship
from typing import List
from pydantic import BaseModel
import json

app = FastAPI()

# Update the DATABASE_URL to use MySQL
DATABASE_URL = "mysql+mysqlconnector://root:123456@47.100.125.229/xx_test"

# Database Connection
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# SQLAlchemy models
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

# Define Pydantic models for responses
class ItemResponse(BaseModel):
    id: int
    name: str
    user_id: int

# Define Pydantic models for responses
class UserResponse(BaseModel):
    username: str
    password: str

# Dependency to get the database session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Create tables
print("尝试在数据库中创建表格。")
Base.metadata.create_all(bind=engine)
print("成功创建表格。")

# Routes
@app.post("/register/")
def register(user : UserResponse, db: Session = Depends(get_db)):
    print(type(user))
    _ = user.model_dump()
    print(type(_))
    print(_)
    print(_['username'])
    print(_['password'])
    u = User(username=_['username'], password=_['password'])
    print('------------')
    db.add(u)
    print('add...')
    db.commit()
    db.refresh(u)
    print('commit...')
    print(db.query(User).all())
    return user.model_dump()

    # print(f"Received request - username: {username}, password: {password}")
    # # Create user
    # user = User(username=username, password=password)
    # db.add(user)
    # db.commit()
    # db.refresh(user)
    # return {"id": user.id, "username": user.username}

@app.post("/login/")
def login(username: str, password: str, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.username == username, User.password == password).first()
    if user:
        return {"message": "Login successful"}
    else:
        raise HTTPException(status_code=400, detail="Invalid credentials")

@app.post("/create-item/")
def create_item(item_name: str, username: str = Depends(login), db: Session = Depends(get_db)):
    user = db.query(User).filter(User.username == username).first()
    if user:
        item = Item(name=item_name, user_id=user.id)
        db.add(item)
        db.commit()
        db.refresh(item)
        return {"item_name": item.name, "owner": user.username}
    else:
        raise HTTPException(status_code=401, detail="User not found")

@app.get("/get-item/{item_id}/", response_model=ItemResponse)
def get_item(item_id: int, db: Session = Depends(get_db)):
    item = db.query(Item).filter(Item.id == item_id).first()
    if item:
        return item
    else:
        raise HTTPException(status_code=404, detail="Item not found")
