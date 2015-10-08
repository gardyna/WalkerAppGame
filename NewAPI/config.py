# -*- coding: utf-8 -*-
# 
# # Statement for enabling the development environment
DEBUG = True

# Define the application directory
import os
BASE_DIR = os.path.abspath(os.path.dirname(__file__))  

# Define the database - we are working with
# SQLite for this example
SQLALCHEMY_DATABASE_URI = 'sqlite:///' + os.path.join(BASE_DIR, 'app.db')
DATABASE_CONNECT_OPTIONS = {}

# Application threads. A common general assumption is
# using 2 per available processor cores - to handle
# incoming requests using one and performing background
# operations using the other.
THREADS_PER_PAGE = 2

# Enable protection agains *Cross-site Request Forgery (CSRF)*
# Hafa þetta false svo hægt sé að senda json * ATH
CSRF_ENABLED     = False

# Use a secure, unique and absolutely secret key for
# signing the data. 
CSRF_SESSION_KEY = "UltimateSecret"

# Secret key for signing cookies
SECRET_KEY = "SvakalegaLeynilegurStrengur"


# For Jsonify
# Set True for pretty print
# Set False for compressed, ugly print
JSONIFY_PRETTYPRINT_REGULAR = True