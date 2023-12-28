import os
import sys
from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float
from sqlalchemy.orm import relationship

from database import Base

class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    name = Column(String(255))
    nickname = Column(String(255))
    sex = Column(String(255))
    headshot = Column(String(255))
    account = Column(String(255))
    phone = Column(String(255))
    password = Column(String(255))

    # Define the relationship with Battery
    battery = relationship("Battery", uselist=False, back_populates="user")

    # Define the relationship with Device
    devices = relationship("Device", back_populates="user")

    # Define the relationship with Log
    logs = relationship("Log", back_populates="user")

    # Define the relationship with TS
    ts = relationship("TS", back_populates="user")


class Battery(Base):
    __tablename__ = 'batteries'

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))

    # Define the relationship with User
    user = relationship("User", back_populates="battery")
    

class Device(Base):
    __tablename__ = 'devices'

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    name = Column(String(255))
    created_time = Column(String(255))
    energy = Column(Integer)
    icon = Column(Integer)

    # Define the relationship with User
    user = relationship("User", back_populates="devices")
    

class Log(Base):
    __tablename__ = 'logs'

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    text = Column(String(255))
    
    # Define the relationship with User
    user = relationship("User", back_populates="logs")

class TS(Base):
    __tablename__ = 'ts'

    id = Column(Integer, primary_key=True)
    data = Column(Float)
    user_id = Column(Integer, ForeignKey('users.id'))
    
    # Define the relationship with User
    user = relationship("User", back_populates="ts")
