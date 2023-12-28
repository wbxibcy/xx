from fastapi import Depends, FastAPI, HTTPException, Query
from sqlalchemy.orm import Session
import arima
from mysql.connector.errors import IntegrityError
import crud, models, schemas
from crud import *
from models import *
from schemas import *
from database import SessionLocal, engine

models.Base.metadata.create_all(bind=engine)

app = FastAPI()

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


# Routes for /login
# 账号密码登录 
@app.post("/login/account-password")
def login_account_password(password: UserPassword, db: Session = Depends(get_db)):
    _ = password.model_dump()
    print(get_password_by_account(db=db, account=_['account']))
    if _['password'] == get_password_by_account(db=db, account=_['account'])[0]:
        return {'status' : 200,
                'id' : get_password_by_account(db=db, account=_['account'])[1]}
    elif _['password'] != get_password_by_account(db=db, account=_['account'])[0]:
        return {'status' : 422}
    else:
        return {'status' : 460}
    
# 手机号 返回用户ID
@app.post("/login/phone-number")
def login_phone_number(phone: UserPhone, db: Session = Depends(get_db)):
    _ = phone.model_dump()
    result = get_user_id_by_phone(db=db, phone=_['phone'])
    print(result)
    if result:
        return {'status' : 200,
                'id': result}
    else:
        return {'status' : 460}

# 忘记密码 更新密码
@app.put("/login")
def forget_password(accountupdate: UserAccountUpdate, db: Session = Depends(get_db)):
    _ = accountupdate.model_dump()
    result = update_password_by_account(db=db, account=_['account'], new_password=_['newpassword'])
    if result:
        return result
    else:
        return {'status' : 460}

# 获取用户ID的所有信息
@app.get("/me")
async def get_login(uid: int = Query(..., description="User ID"), db: Session = Depends(get_db)):
    result = get_user_details_by_id(db=db, user_id=uid)
    if result:
        return result
    else:
        return {'status' : 460}


# Routes for /signup
# 注册用户
@app.post("/signup")
def signup(user: schemas.UserCreate, db: Session = Depends(get_db)):
    _ = user.model_dump()
    print(_)
    result = crud.create_user(db, user)
    if result:
        return result
    else:
        return {'status' : 460}


# Routes for /dashboard
# 获取设备信息
@app.post("/dashboard")
def home(user_id: UserId, db: Session = Depends(get_db)):
    try:
        _ = user_id.model_dump()
        result = crud.get_device_details_by_user_id(db, user_id=_['uid'])
        if not result:
            result = {'status' : 'uid not found'}
            return result
        return result
    finally:
        if 'result' not in locals():
            result = {'status': 460}
        return result


# Routes for /devices
# 创建设备
@app.post("/devices")
def create_device(device: schemas.DeviceCreate, db: Session = Depends(get_db)):
    try:
        result = crud.create_device(db, device)
        return result
    except IntegrityError:
        result = {'status' : 'uid not found'}
        return result
    finally:
        if 'result' not in locals():
            result = {'status': 460}
        return result

# 根据设备名字获取设备详细信息
@app.get("/devices")
async def get_device_details_by_name(dname: str = Query(..., description="Device Name"), db: Session = Depends(get_db)):
    result = crud.get_device_details_by_name(db, device_name=dname)
    if result:
        return result
    else: 
        return {'status' : 460}

# 更新设备信息
@app.put("/devices")
def update_device_details(updated_device: DeviceUpdate, db: Session = Depends(get_db)):
    print(updated_device.model_dump()['did'])
    if crud.update_device_details(db, updated_device.model_dump()['did'], updated_device):
        return {'status' : 200}
    else:
        return {'status' : 460}

# 删除设备
@app.delete("/devices")
def delete_device(device_id: DeviceID, db: Session = Depends(get_db)):
    if crud.delete_device(db, device_id.model_dump()['did']):
        return {'status' : 200}
    else:
        return {'status' : 'device not found'}
    

# Routes for /batteries
@app.get("/ts")
async def get_ts_data(uid: int = Query(..., description="User ID"), db: Session = Depends(get_db)):
    result = get_ts_data_by_user_id(db=db, user_id=uid)
    print(result)
    prediction = arima.time_series_prediction(result)
    print(prediction)
    if result:
        return {'result' : result,
                'prediction' : prediction}
    else:
        return {'status' : 460}

# Routes for /batteries/advices
# 获取电池的建议
@app.get("/batteries-advices")
async def get_battery_advices(uid: int = Query(..., description="User ID"), db: Session = Depends(get_db)):
    result = get_logs_by_user_id(db=db, user_id=uid)
    if result:
        return result
    else:
        return {'status' : 460}

# Routes for /users
# 获得用户所有信息
@app.get("/users")
async def get_user(uid: int = Query(..., description="User ID"), db: Session = Depends(get_db)):
    result = get_user_details_by_id(db=db, user_id=uid)
    if result:
        return result
    else:
        return {'status' : 460}

# 更新用户信息
@app.put("/users")
def update_user(updated_user: UserUpdate, db: Session = Depends(get_db)):
    _ = updated_user.model_dump()
    print(_)
    result = update_user_details(db=db, user_id=_['uid'], updated_user=updated_user)
    if result:
        return result
    else: 
        return {'status' : 460}

# 删除用户
@app.delete("/users")
def delete_user(user_id: UserId, db: Session = Depends(get_db)):
    _ = user_id.model_dump()
    print(_)
    if crud.delete_user(db, user_id=_['uid']):
        return {'status' : 200}
    else:
        return {'status' : 'uid not found'}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1")
