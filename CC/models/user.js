const {Sequelize, DataTypes} = require('sequelize')
const dotenv = require('dotenv')
const Pesan = require('./pesan.js')
dotenv.config()

const DB_HOST = process.env.DB_HOST
const DB_USER = process.env.DB_USER
const DB_PASS = process.env.DB_PASS
const DB_NAME = process.env.DB_NAME

const sequelize = new Sequelize(`mysql://${DB_USER}:${DB_PASS}@${DB_HOST}:3306/${DB_NAME}`)

const User = sequelize.define('user', {
    id_user: {
        type: DataTypes.STRING, 
        primaryKey: true, 
        allowNull: false,

    },
    nama: {
       type: DataTypes.STRING(255),
       allowNull: false

    },
    
    username: {
        type: DataTypes.STRING(255),
        allowNull: false
 
     },

     no_hp: {
        type: DataTypes.STRING(16),
        allowNull: false
 
     },

     password: {
        type: DataTypes.STRING(255),
        allowNull: false
 
     },
},{sequelize, modelName:'user'})
Pesan.belongsTo(User, {
   foreignKey: 'id_user', 
   onDelete: 'CASCADE',  
   onUpdate: 'CASCADE'
   
})
User.hasMany(Pesan, {foreignKey:'id_user'})
module.exports = User