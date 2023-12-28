from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError
import models, schemas

# CRUD operations for User

def create_user(db: Session, user: schemas.UserCreate):
    try:
        db_user = models.User(**user.model_dump())
        db.add(db_user)
        db.commit()
        db.refresh(db_user)
        return db_user
    except IntegrityError:
        db.rollback()
        return None

def get_password_by_account(db: Session, account: str):
    user = db.query(models.User).filter(models.User.account == account).first()
    return user.password, user.id if user else None

def update_password_by_account(db: Session, account: str, new_password: str):
    user = db.query(models.User).filter(models.User.account == account).first()
    if user:
        user.password = new_password
        db.commit()
        db.refresh(user)
        return user
    return None

def get_user_id_by_phone(db: Session, phone: str):
    user = db.query(models.User).filter(models.User.phone == phone).first()
    return user.id if user else None

def get_user_details_by_id(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()

def delete_user(db: Session, user_id: int):
    user = db.query(models.User).filter(models.User.id == user_id).first()
    if user:
        db.delete(user)
        db.commit()
        return user
    return None

def update_user_details(db: Session, user_id: int, updated_user: schemas.User):
    user = db.query(models.User).filter(models.User.id == user_id).first()
    if user:
        for key, value in updated_user.model_dump(exclude_unset=True).items():
            setattr(user, key, value)
        db.commit()
        db.refresh(user)
        return user
    return None

# CRUD operations for Device

def create_device(db: Session, device: schemas.DeviceCreate):
    db_device = models.Device(**device.model_dump())
    db.add(db_device)
    db.commit()
    db.refresh(db_device)
    return db_device

def get_user_id_by_name(db: Session, username: str):
    user = db.query(models.User).filter(models.User.name == username).first()
    return user.id if user else None

def get_device_details_by_user_id(db: Session, user_id: int):
    return db.query(models.Device).filter(models.Device.user_id == user_id).all()

def get_device_details_by_name(db: Session, device_name: str):
    return db.query(models.Device).filter(models.Device.name == device_name).first()

def update_device_details(db: Session, device_id: int, updated_device: schemas.Device):
    db_device = db.query(models.Device).filter(models.Device.id == device_id).first()
    if db_device:
        for key, value in updated_device.model_dump(exclude_unset=True).items():
            setattr(db_device, key, value)
        db.commit()
        db.refresh(db_device)
        return db_device
    return None

def delete_device(db: Session, device_id: int):
    db_device = db.query(models.Device).filter(models.Device.id == device_id).first()
    if db_device:
        db.delete(db_device)
        db.commit()
        return db_device
    return None


# Other CRUD operations

# def get_battery_id_by_user_id(db: Session, user_id: int):
#     battery = db.query(models.Battery).filter(models.Battery.user_id == user_id).first()
#     return battery.id if battery else None

def get_ts_data_by_user_id(db: Session, user_id: int):
    ts_data = db.query(models.TS.data).filter(models.TS.user_id == user_id).limit(20).all()
    return [data[0] for data in ts_data] if ts_data else None

def get_logs_by_user_id(db: Session, user_id: int):
    return db.query(models.Log).filter(models.Log.user_id == user_id).all()

def get_user_profile_details(db: Session, user_id: int):
    user = db.query(models.User).filter(models.User.id == user_id).first()
    return {"nickname": user.nickname, "headshot": user.headshot, "sex": user.sex, "phone": user.phone} if user else None
