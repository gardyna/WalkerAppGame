# standard lib
from functools import wraps

# third party packages
from flask import Flask, jsonify, abort, request, Response
from flask.ext.sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.debug = True
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test.db'
app.secret_key = 'A0Zr98j/3yX R~XHH!jmN]LWX/,?RT'
db = SQLAlchemy(app)


# region dbClasses


class User(db.Model):
    """
    Represent a user in database
    """
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True)
    email = db.Column(db.String(120), unique=True)

    def __init__(self, username, email):
        self.username = username
        self.email = email

    def __repr__(self):
        return '<User {}>'.format(self.username)

    def to_dict(self):
        return {
            'id': self.id,
            'username': self.username,
            'email': self.email
        }
# endregion

# region authorization


def check_auth(username, password):
    return username == 'admin' and password == 'secret'


def authenticate():
    return Response(
    'Could not verify your access level for that URL.\n'
    'You have to login with proper credentials', 401,
    {'WWW-Authenticate': 'Basic realm="Login Required"'})


def requires_auth(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        auth = request.authorization
        if not auth or not check_auth(auth.username, auth.password):
            return authenticate()
        return f(*args, **kwargs)
    return decorated

# endregion


@app.route('/')
@requires_auth
def hello_world():
    return 'Hello World!'


@app.route('/users', methods=['GET', 'POST'])
def get_users():
    if request.method == 'POST':
        if (request.json['username'] is None
                or request.json['email'] is None):
            abort()
        user = User(request.json['username'],
                    request.json['email'])
        db.session.add(user)
        db.session.commit()
        return jsonify({'user': user.to_dict()}), 201
    elif request.method == 'GET':
        users = User.query.all()
        users_dto = [user.to_dict() for user in users]
        return jsonify({'users': users_dto}), 200
    else:
        abort(405,  "Method not supported")


@app.errorhandler(405)
def custom405(error):
    response = jsonify({'message': error.description})
    return response, 405


if __name__ == '__main__':
    if app.debug:
        app.run()
    else:
        app.run(host='0.0.0.0')
