import Realm, { schemaVersion, openAsync } from 'realm';
export const TANK_TABLE = 'TankTable';
export const PROJECTILE_TABLE = 'ProjectileTable'

export const TankTableSchema = {
    name: TANK_TABLE,
    primaryKey: 'Id',
    properties: {
        Id: 'int',
        Name: {type: 'string', indexed: true},
        Weight: {type: 'int', indexed:true},
        Gun: {type: 'int', indexed:true},
        Country: {type: 'string',indexed:true},
        Number: {type: 'int', indexed:true},
    }
};

export const ProjectileTableSchema = {
    name: PROJECTILE_TABLE,
    primaryKey: 'Id',
    properties:{
        Id : 'int',
        name: {type: 'string', indexed: true},
        typep: {type: 'string', indexed:true},
        caliber: {type: 'int', indexed:true},
        number: {type: 'int', indexed:true},
    }
};

const databaseOption = {
    path: 'TankProjectilesDB.realm',
    schema: [TankTableSchema,ProjectileTableSchema],
    schemaVersion: 0
};

export const insertTank = newTank => new Promise((resolve, reject) =>{
    Realm.open(databaseOption).then(realm => {
        realm.write(() => {
            realm.create(TANK_TABLE, newTank);
            resolve(newTank);
        });
    }).catch((error) => reject(error));   
});

export const insertProjectile = newProjectile => new Promise((resolve,reject) =>{
    Realm.open(databaseOption).then(realm =>{
        realm.write(() =>{
            realm.create(PROJECTILE_TABLE, newProjectile);
            resolve(newProjectile);
        });
    }).catch((error) => reject(error))
});

export const readTanks = () => new Promise((resolve,reject) =>{
    Realm.open(databaseOption).then(realm =>{
        let tankList = realm.objects(TANK_TABLE);
        resolve(tankList)
    }).catch((error) => {
        reject(error);
    });
});

export const readProjectiles = () => new Promise((resolve,reject) =>{
    Realm.open(databaseOption).then(realm =>{
        let projectileList = realm.objects(PROJECTILE_TABLE);
        resolve(projectileList)
    }).catch((error) => {
        reject(error);
    });
});

export const filterTank = tankSearch => new Promise((resolve,reject) =>{
    Realm.open(databaseOption).then(realm =>{
        let tankfilter = realm.objects(TANK_TABLE)
        .filtered(tankSearch);
        resolve(tankfilter);
    }).catch((error) => {reject(error);});
});

export const filterProjectile = projectileSearch => new Promise((resolve,reject) => {
    Realm.open(databaseOption).then(realm =>{
        let projectilefilter = realm.objects(PROJECTILE_TABLE)
        .filtered(projectileSearch);
        resolve(projectilefilter);
    }).catch((error) => {reject(error);});
});


export const updateTank = tankItem => new Promise((resolve,reject)=>{
    Realm.open(databaseOption).then(realm=>{
        realm.write(() => {
            let updateTanks = realm.objects(TANK_TABLE)
            updateTanks.forEach(element => {
                if (tankItem.Name === element.Name)
                {
                    if (tankItem.Country != "")
                        element.Country = tankItem.Country
                    if (tankItem.Weight != "")
                        element.Weight = parseInt(tankItem.Weight)
                    if (tankItem.Gun != "")
                        element.Gun = parseInt(tankItem.Gun)
                    if (tankItem.number != "")
                        element.Number = parseInt(tankItem.Number)
                }
            });
            resolve();
        });
    }).catch((error) => reject(error));
});

export const updateProjectile = projectileItem => new Promise((resolve,reject)=>{
    Realm.open(databaseOption).then(realm=>{
        realm.write(() => {
            let updateProjectiles = realm.objects(PROJECTILE_TABLE)
            updateProjectiles.forEach(element => {
                if (projectileItem.name === element.name)
                {
                    if (projectileItem.typep != "")
                        element.typep = projectileItem.typep
                    if (projectileItem.caliber != "")
                        element.caliber = parseInt(projectileItem.caliber)
                    if (projectileItem.number != "")
                        element.number = parseInt(projectileItem.number)
                }
            });
            resolve();
        });
    }).catch((error) => reject(error));
});

export const deleteTank = tankItemD => new Promise((resolve,reject) =>{
    Realm.open(databaseOption).then(realm=>{
        let delTank = realm.objects(TANK_TABLE).filtered("Name = " + "'" + tankItemD + "'");
        realm.write(() => {
            realm.delete(delTank);
            resolve();
        });
    }).catch((error) => reject(error));
});

export const deleteProjectile = projectileItem => new Promise((resolve,reject) =>{
    Realm.open(databaseOption).then(realm=>{
        let delProjectiles = realm.objects(PROJECTILE_TABLE).filtered("name = " + "'" + projectileItem + "'");
        realm.write(() => {
            realm.delete(delProjectiles);
            resolve();
        });
    }).catch((error) => reject(error));
});

export default new Realm(databaseOption);