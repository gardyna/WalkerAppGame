# -*- coding: utf-8 -*-


from flask import Flask, Blueprint, request, abort, jsonify



from itsdangerous import (TimedJSONWebSignatureSerializer
                          as Serializer, BadSignature, SignatureExpired)

from app import db, app, auth, return_response

# # Import module models (i.e. User)
from app.mod_user.models import User

# Define the blueprint: 'auth', set its url prefix: app.url/auth
mod_user = Blueprint('user', __name__, url_prefix='/user')

@mod_user.route("/all/", methods=['GET'])
def getAllUsers():
	users = User.query.all()
	allUsers = [u.username for u in users]
	print allUsers
	return return_response(200, "OK", allUsers)



@mod_user.route("/getTokens/<username>/", methods=['GET'])
def getToken(username):
	user = User.query.filter_by(username = username).first()

	if user is None:
		return return_response(404, "User not found", {})

	token= user.generate_token_key()
	response = return_response(200, "ok", {"token":token})
	return response



@mod_user.route("/get/", methods=['GET'])
@mod_user.route("/get/<username>/", methods=['GET'])
def getUsersOrById(username=None):
	if username is None:
		users = User.query.all()
		result = [user.username for user in users]
	else:
		user = User.query.filter_by(username=username).first()
		if user is None:
			return return_response(404, "No user found with this username", {})
		else:
			result = [{user}]

	return return_response(200, "OK", result)