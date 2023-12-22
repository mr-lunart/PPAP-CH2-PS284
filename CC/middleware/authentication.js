const jwt = require('jsonwebtoken')
const User = require('../models/user.js')

const authenticateToken = async(req, res, next) => {
    const authHeader = req.headers['authorization']
    const token = authHeader && authHeader.split(' ')[1]
    if (token == null || !token) return res.status(400).json({ message: 'Token invalid' })
    jwt.verify(token, process.env.SECRET_KEY, (error, decoded) => {
        if (error) return res.status(400).json({ message: 'Token expired' })
        req.username = decoded.username
        next()
    })
}

module.exports = authenticateToken