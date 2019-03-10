import React, { Component } from 'react';
import { Text, View, Alert, Platform } from 'react-native';
const apiGetTanks = 'http://a409b0dc.ngrok.io/api/Tank';
const apiGetProjectiles = 'http://a409b0dc.ngrok.io/api/Projectile';

async function getTanks() {
    try{
        let response = await fetch(apiGetTanks);
        let responseJson = await response.json();
        return responseJson;
    }catch(error){  }
}

async function getProjectiles(){
    try{
        let response = await fetch(apiGetProjectiles);
        let responseJson = await response.json();
        return responseJson
    }catch(error){   }
}

async function addTank(tankitem){
    try{
        let response = await fetch(apiGetTanks,{
            method: 'POST',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify(tankitem)            
        });
    }catch(error){
        console.error(`error is: ${error}`);
    }
}

async function addProjectile(projectileitem){
    let projectileItem = {
        Name: projectileitem.name,
        Type: projectileitem.typep,
        Caliber: projectileitem.caliber,
        Number: projectileitem.number
    };

    try{
        let response = await fetch(apiGetProjectiles,{
            method: 'POST',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify(projectileItem)            
        });
    }catch(error){
        console.error(`error is: ${error}`);
    }
}

async function updateTankS(tanklist,tankItem){
    let id
    tanklist.array.forEach(element => {
        if (element.Name === tankItem.Name){
            id = element.Id
            if (tankItem.Country === "")
                tankItem.Country = element.Country
            if (tankItem.Weight === "")
                tankItem.Weight = parseInt(element.Weight)
            if (tankItem.Gun === "")
                tankItem.Gun = parseInt(element.Gun)
            if (tankItem.number === "")
                tankItem.Number = parseInt(element.Number)
        }
    });

    let apiUpdate = apiGetTanks + '/' + id; 
    try{
        let response = await fetch(apiUpdate, {
            method: 'PUT',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify(tankItem)
        });
        return "OK";
    }catch(error){
        console.error(`error is : ${error}`);
        return "NOT OK";
    }
}

async function updateProjectileS(projectilelist, projectileitem){
    let id;
    let projectileItem = {
        Name : "",
        Type : "",
        Caliber : 0,
        Number : 0,
    };

    projectilelist.array.forEach(element => {
        if (element.name === projectileitem.name){
            id = element.Id
            projectileItem.Name = element.name

            if (projectileitem.typep === "")
                projectileItem.Type = element.typep
            else
                projectileItem.Type = projectileitem.typep

            if (projectileitem.caliber === "")
                projectileItem.Caliber = parseInt(element.caliber)
            else
                projectileItem.Caliber = parseInt(projectileitem.caliber)

            if (projectileitem.number === "")
                projectileItem.Number = parseInt(element.number)
            else
                projectileItem.Number = parseInt(projectileitem.number)
        }
    });

    let apiUpdate = apiGetProjectiles + '/' + id; 
    try{
        let response = await fetch(apiUpdate, {
            method: 'PUT',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify(projectileItem)
        });
        return "OK";
    }catch(error){
        Alert.alert(`error is : ${error}`);
        return "NOT OK";
    }
}

async function deleteTankS(id){
    try{
        let response = await fetch(apiGetTanks + '/' + id,{
            method : "DELETE",
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json',
            },
        });
        return "OK";
    }catch(error){
        console.error(`error is : ${error}`);
        return "NOT OK";
    }
}

async function deleteProjectileS(id){
    try{
        let response = await fetch(apiGetProjectiles + '/' + id,{
            method: 'DELETE',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json',
            },
        });
        return "OK";
    }catch(error){
        console.error(`error is : ${error}`);
        return "NOT OK";
    }
}

export {getTanks};
export {getProjectiles};
export {addProjectile};
export {addTank};
export {updateTankS};
export {updateProjectileS};
export {deleteTankS};
export {deleteProjectileS};