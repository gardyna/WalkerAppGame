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
    userId= db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False, index=True)
    friendId= db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False, index=True)

    # Two indexes so we can search friendship easily
    __table_args__ = (Index('friends1', "userId", "friendId"), 
        Index('friends2', "friendId", "userId"), )

    def __init__(self, userId, friendId):
        self.userId = userId
        self.friendId = friendId

    def __repr__(self):
        return '<User %s>' % (self.username)