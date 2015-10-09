# -*- coding: utf-8 -*-

from flask import jsonify
from flask.ext.bcrypt import generate_password_hash

from sqlalchemy import Index

from itsdangerous import (TimedJSONWebSignatureSerializer
                          as Serializer, BadSignature, SignatureExpired)

from app import db, app

class Friends(db.Model):
    """
    Represent a friends in database
    """
    id = db.Column(db.Integer, primary_key=True)
    userId= db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    friendId= db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)

    friends1 = db.Index("Friends1", "userId", "friendId", unique=True)
    friends2 = db.Index("Friends2", "friendId", "userId", unique=True)

    def __init__(self, userId, friendId):
        self.userId = userId
        self.friendId = friendId

    def __repr__(self):
        return '<User %s>' % (self.username)