# -*- coding: utf-8 -*-


from app import app, db
from config import *

app.run(host="0.0.0.0", port=8080, debug=True)
