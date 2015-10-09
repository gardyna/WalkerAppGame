# -*- coding: utf-8 -*-
# 

from flask import Flask, Blueprint, make_response, jsonify, request
from flask.ext.bcrypt import check_password_hash

from app import db, app, return_response

# # Import module models (i.e. User)
from app.mod_user.models import User

# Define the blueprint: 'auth', set its url prefix: app.url/auth
mod_index = Blueprint('index', __name__, url_prefix='/')

@mod_index.route("login/", methods=['POST'])
def login():
	username = request.json.get("username")
	password = request.json.get("password")

	user = User.query.filter_by(username = username).first()

	if user is None or not check_password_hash(user.password, password):
		return return_response(400, "Wrong input")
		#return jsonify({'error':'wronginput'}), 400


	# Return token key to user
	return return_response(200, "OK", {'token':user.generate_token_key()})
	#return jsonify({'toke':user.generate_token_key()})

# Register new user
@mod_index.route("register/", methods=['POST'])
def new_user():
	username = request.json.get('username')
	email = request.json.get('email')
	password = request.json.get('password')

	print username,email,password
	if username is "" or email is "" or password is "":
		return jsonify({'result':'missing'}), 400

	alreadyRegisterd = User.query.filter_by(username = username).first()

	if alreadyRegisterd is not None:
		return jsonify({'result':'exist'}), 400

	newUser = User(username, email, password)
	db.session.add(newUser)
	db.session.commit()

	# Return that the user was created but he
	return jsonify({'result':'created'}), 201

