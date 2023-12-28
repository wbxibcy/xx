from typing import Optional
from pydantic import BaseModel
from typing import Union

class UserBase(BaseModel):
    name: str
    nickname: str
    sex: str
    headshot: Optional[str]
    account: str
    phone: str

class UserCreate(UserBase):
    password: str

class UserPassword(BaseModel):
    account: str
    password: str

class UserPhone(BaseModel):
    phone: str

class UserAccount(BaseModel):
    account: str

class UserAccountUpdate(BaseModel):
    account: str
    newpassword: str

class UserId(BaseModel):
    uid: int

class UserUpdate(UserBase):
    uid: int

class User(UserBase):
    uid: Optional[int]

    class Config:
        orm_mode = True

class BatteryBase(BaseModel):
    pass

class BatteryCreate(BatteryBase):
    user_id: int

class Battery(BatteryBase):
    bid: Optional[int]
    user: User

    class Config:
        orm_mode = True

class DeviceBase(BaseModel):
    name: str
    created_time: str
    energy: int

class DeviceCreate(DeviceBase):
    user_id: int

class DeviceName(BaseModel):
    name: str

class DeviceID(BaseModel):
    did: int

class DeviceUpdate(DeviceBase):
    did: int

class Device(DeviceBase):
    did: Optional[int]
    user: User

    class Config:
        orm_mode = True

class LogBase(BaseModel):
    text: str

class LogCreate(LogBase):
    user_id: int

class Log(LogBase):
    lid: Optional[int]
    user: User

    class Config:
        orm_mode = True

# class TS(BaseModel):
#     # tid: Optional[int]
#     # time: str
#     data: float
#     user_id: int
