#!/bin/bash

lein do clean, ring uberjar && docker build -t ebot-api .
