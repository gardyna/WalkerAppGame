# -*- coding: utf-8 -*-


from flask import Flask, Blueprint, request, abort, jsonify, g



from itsdangerous import (TimedJSONWebSignatureSerializer
                          as Serializer, BadSignature, SignatureExpired)

from app import db, app, auth, return_response, login_required

# # Import module models (i.e. User)
from app.mod_user.models import User
from app.mod_friends.models import Friends

# Define the blueprint: 'auth', set its url prefix: app.url/auth
mod_friends = Blueprint('friends', __name__, url_prefix='/friends')

@mod_friends.route("/", methods=['GET'])
@login_required
def getMyFriends():
	return return_response(200, "OK")

@mod_friends.route("/", methods=['POST'])
@login_required
def addFriend():
	username = request.json.get("username")
	friend = User.query.filter_by(username=username).first()

	newFriend = Friends(g.user.id, friend.id)
	db.session.add(newFriend)
	db.session.commit()

	return return_response(201, "OK")