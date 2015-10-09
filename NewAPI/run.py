# -*- coding: utf-8 -*-


from app import app, db
from config import *

app.run(host="127.0.0.1", port=8080, debug=True)
