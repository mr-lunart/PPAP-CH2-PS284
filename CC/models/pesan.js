const {Sequelize, DataTypes} = require('sequelize')
const dotenv = require('dotenv')

dotenv.config()

const DB_HOST = process.env.DB_HOST
const DB_USER = process.env.DB_USER
const DB_PASS = process.env.DB_PASS
const DB_NAME = process.env.DB_NAME

const sequelize = new Sequelize(`mysql://${DB_USER}:${DB_PASS}@${DB_HOST}:3306/${DB_NAME}`)

const Pesan = sequelize.define('pesan', {
    id_pesan: {
        type: DataTypes.STRING, 
        primaryKey: true, 
        allowNull: false,

    },
    id_user: {
       type: DataTypes.STRING(255),
       allowNull: false, references: {model: 'users', key: 'id_user'}

    },
    
    tanggal: {
        type: DataTypes.DATE,
        allowNull: false
 
     },

     id_rumah_sakit: {
        type: DataTypes.STRING(255),
        allowNull: false, references: {model: 'rumah_sakits', key: 'id_rumah_sakit'}
 
     },

     status: {
        type: DataTypes.STRING(255),
        allowNull: false
 
     },
},{sequelize, modelName:'pesan'})

module.exports = Pesan