# -*- coding: utf-8 -*-

from flask import jsonify
from flask.ext.bcrypt import generate_password_hash

from itsdangerous import (TimedJSONWebSignatureSerializer
                          as Serializer, BadSignature, SignatureExpired)

from app import db, app

class User(db.Model):
    """
    Represent a user in database
    """
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False, index=True)
    email = db.Column(db.String(120), unique=True, nullable=False)
    password = db.Column(db.String(120), nullable=False)

    def __init__(self, username, email, password):
        self.username = username
        self.email = email
        self.password = generate_password_hash(password)

    def __repr__(self):
        return '<User %s>' % (self.username)

    def generate_token_key(self):
        s = Serializer(app.config['SECRET_KEY'])
        return s.dumps({'username':self.username})


    @staticmethod
    def verify_auth_token(token):
        s = Serializer(app.config['SECRET_KEY'])
        try:
            data = s.loads(token)
        except BadSignature:
            return None # invalid token

        # Get user by username. If user doesn't exists None is returned
        user = User.query.filter_by(username = (data['username'])).first()
        return user


    def to_dict(self):
        return {
            'id': self.id,
            'username': self.username,
            'email': self.email
        }