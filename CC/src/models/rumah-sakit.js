const {Sequelize, DataTypes} = require('sequelize')
const dotenv = require('dotenv')
const Pesan = require('./pesan.js')
dotenv.config()

const DB_HOST = process.env.DB_HOST
const DB_USER = process.env.DB_USER
const DB_PASS = process.env.DB_PASS
const DB_NAME = process.env.DB_NAME

const sequelize = new Sequelize(`mysql://${DB_USER}:${DB_PASS}@${DB_HOST}:3306/${DB_NAME}`)

const RumahSakit = sequelize.define('rumah_sakit', {
    id_rumah_sakit: {
        type: DataTypes.STRING, 
        primaryKey: true, 
        allowNull: false,

    },
    nama: {
       type: DataTypes.STRING(255),
       allowNull: false

    },
    
    alamat: {
        type: DataTypes.STRING(255),
        allowNull: false
 
     },

     no_hp: {
        type: DataTypes.STRING(16),
        allowNull: false
 
     },

     latitude: {
        type: DataTypes.STRING(255),
        allowNull: false
 
     },

     longitude: {
        type: DataTypes.STRING(255),
        allowNull: false
 
     },

     type: {
        type: DataTypes.STRING(6),
        allowNull: false
 
     },
},{sequelize, modelName:'rumah_sakit'})
Pesan.belongsTo(RumahSakit, {
   foreignKey: 'id_rumah_sakit', 
   onDelete: 'CASCADE',  
   onUpdate: 'CASCADE'
   
})
RumahSakit.hasMany(Pesan, {foreignKey:'id_rumah_sakit'})
module.exports = RumahSakit