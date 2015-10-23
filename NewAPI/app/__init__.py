# -*- coding: utf-8 -*-
# 
# # Import flask and template operators
from flask import Flask, Blueprint, g, jsonify, request

from functools import wraps

# Import SQLAlchemy
from flask.ext.sqlalchemy import SQLAlchemy
from flask.ext.httpauth import HTTPBasicAuth


# Application
app = Flask(__name__)
app.config.from_object('config')

# Authentication
auth = HTTPBasicAuth()

# Database connection
db = SQLAlchemy(app)


# Authentication method - new one
def login_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        # Import User model
        from mod_user.models import User

        token = request.headers.get('Authorization')
        user = User.verify_auth_token(token)
        if user is None:
            return return_response(404, "Not found", {})
        g.user = user
        return f(*args, **kwargs)
    return decorated_function

# OLD method
# @auth.login_required
# Here we implement authentication
@auth.verify_password
def verify_password(username_or_token, password):
    # Import User model
    from mod_user.models import User

    # first try to authenticate by token
    user = User.verify_auth_token(username_or_token)

    if not user:
    	return False
        ## try to authenticate with username/password
        #user = User.query.filter_by(username = username_or_token).first()
        #if not user or not user.verify_password(password):
        #    return False
    g.user = user
    return True

# Just print out response for debugging
@app.after_request
def per_request_callbacks(response):
    print response.data
    for func in getattr(g, 'call_after_request', ()):
        response = func(response)
        print dir(response)
    return response

# Special repsonse for all responses
def return_response(status, message="", result={}):
    response = {"meta": {"status":status,"message":message},"content":result}
    response = jsonify(response)
    return response, status


def error_response(error):
    return return_response(error.code, error.name)

# All possible errors
for error in (400,404,500):
    app.error_handler_spec[None][error] = error_response

# Import Blueprint modules
from app.mod_index.controllers import mod_index as indexModule
from app.mod_user.controllers import mod_user as userModule
from app.mod_friends.controllers import mod_friends as friendsModule

# Register them
app.register_blueprint(indexModule)
app.register_blueprint(userModule)
app.register_blueprint(friendsModule)

# Finally create all the database
db.create_all()