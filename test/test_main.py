import requests

data = {
    'username': "eva",
    'password': "123"
}
res = requests.post("http://127.0.0.1:8000/register/", json=data)
print(res.text)