/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {StyleSheet, Button, View, Text, Modal, TextInput,FlatList,Alert} from 'react-native';
import {insertTank, insertProjectile,readProjectiles,readTanks,filterTank, filterProjectile,updateProjectile,updateTank,deleteProjectile,deleteTank } from '../ReactProject/database/db';
import realm from '../ReactProject/database/db';
import {getTanks,getProjectiles,addProjectile,addTank,updateProjectileS,updateTankS,deleteProjectileS,deleteTankS} from '../ReactProject/database/server';

var connection = 1;

class TankItemList extends Component{
  render() {
    return(
      <View style = {{
          flex:1,
          backgroundColor: this.props.index % 2 == 0 ? '#A4A5A4': '#CDCDCD',
          marginTop:10
      }}> 
          <View style = {{flex:1, justifyContent:'center'}}><Text style={styles.tankListItem}> {this.props.item.Name} </Text></View>
          <View style ={{flex:1,
                        backgroundColor: this.props.index % 2 == 0 ? '#A4A5A4': '#CDCDCD',
                        flexDirection: 'row'}}>
              <Text style={styles.textlist}>Weight:</Text>
              <Text style= {styles.textcontent}> {this.props.item.Weight}Tonnes </Text> 
              <Text style={styles.textlist}>Gun:</Text>
              <Text style= {styles.textcontent}> {this.props.item.Gun}mm </Text> 
          </View>
          <View style ={{flex:1,
                        backgroundColor: this.props.index % 2 == 0 ? '#A4A5A4': '#CDCDCD',
                        flexDirection: 'row'}}>
              <Text style={styles.textlist}>Country:</Text>
              <Text style= {styles.textcontent}> {this.props.item.Country} </Text> 
              <Text style={styles.textlist}>Number:</Text>
              <Text style= {styles.textcontent}> {this.props.item.Number} </Text> 
          </View>
          <Button
                title = "Delete"
                onPress = {() => {
                  deleteTankS(this.props.item.Id).then((response) =>{
                    if (response === "OK")
                      deleteTank(this.props.item.Name).then().catch((error) => {Alert.alert("e"+error);});
                    else
                      Alert.alert("No connection");
                  }).catch((error) => {Alert.alert("No connection");});
                }}/>
      </View>
    );
  }
}

class ProjectileItemList extends Component{
  render() {
    return(
      <View style = {{
        flex:1,
        backgroundColor: this.props.index % 2 == 0 ? '#A4A5A4': '#CDCDCD',
        marginTop:10
      }}> 
        <View style = {{flex:1, justifyContent:'center'}}><Text style={styles.tankListItem}> {this.props.item.name} </Text></View>
        <View style ={{flex:1,
                      backgroundColor: this.props.index % 2 == 0 ? '#A4A5A4': '#CDCDCD',
                      flexDirection: 'row'}}>
            <Text style={styles.textlist}>Type:</Text>
            <Text style= {styles.textcontent}> {this.props.item.typep} </Text> 
            <Text style={styles.textlist}>Caliber:</Text>
            <Text style= {styles.textcontent}> {this.props.item.caliber}mm </Text> 
        </View>
        <View style ={{flex:1,
                      backgroundColor: this.props.index % 2 == 0 ? '#A4A5A4': '#CDCDCD',
                      flexDirection: 'row', justifyContent: 'center'}}> 
            <Text style={styles.textlist}>Number:</Text>
            <Text style= {styles.textcontent}> {this.props.item.number} </Text> 
        </View>
        <Button title = "Delete" onPress = {() =>{
          deleteProjectileS(this.props.item.Id).then((response) => {
            if (response === "OK")
              deleteProjectile(this.props.item.name).then().catch(error => {Alert.alert("error "+error);});
            else
              Alert.alert("No connection");
          }).catch((error) => {Alert.alert("No connection");});
          }}/>
    </View>
    );
  }
}
  

type Props = {};
export default class App extends Component<Props> {
  
  constructor(props) {
    super(props);
    this.CheckIfTank = true;
    this.CheckIfProjectile = false;
    this.state ={
    TankList: [],
    ProjectileList: [],
    AddCheckTank: false,
    UpdateCheckTank: false,
    FilterCheckTank: false,
    AddCheckProjectile: false,
    UpdateCheckProjeclite: false,
    FilterCheckProjectile: false,
    Check: true,
    TankName: '',
    TankCountry: '',
    TankGun: 0,
    TankWeight: 0,
    TankNumber: 0,
    TankFilterCountry: '',
    TankFilterGun: '',
    TankFilterWeight: '',
    TankFilterNumber: '',
    ProjectileName: '',
    ProjectileCaliber: 0,
    ProjectileType: '',
    ProjectileNumber: 0,
    ProjectileFilterType: '',
    ProjectileFilterCaliber: '',
    ProjectileFilterNumber: ''
    };
    this.setTank();
    this.reloadData();
    realm.addListener('change', () =>{
      this.reloadDataServer();
    });

  }

  resetData = () => {
    this.setState({
      TankFilterCountry: '',
      TankFilterGun: '',
      TankFilterNumber: '',
      TankFilterWeight: '',
      ProjectileFilterCaliber: '',
      ProjectileFilterNumber: '',
      ProjectileFilterType: '',
      TankName: '',
      TankCountry: '',
      TankGun: 0,
      TankWeight: 0,
      TankNumber: 0,
      ProjectileName: '',
      ProjectileCaliber: 0,
      ProjectileType: '',
      ProjectileNumber: 0
    });
  }

  componentDidMount(){
    this.reloadDataServer();
  }

  reloadDataServer(){
    getTanks().then((tanklist) =>{this.setState({TankList: tanklist});
    }).catch((error) => {
      readTanks().then((tanklist) => { this.setState({TankList: tanklist});
      }).catch((error) => {this.setState({TankList: []});});
    });
    getProjectiles().then((projectilelist) => {
        this.setState({ProjectileList: []});
        projectilelist.forEach((elem) =>{
            let obj = {
              Id : elem.Id,
              name: elem.Name,
              typep: elem.Type,
              caliber: elem.Caliber,
              number: elem.Number,
            }
            this.setState(prevstate => ({ProjectileList: [...prevstate.ProjectileList, obj]}));
        });
    }).catch((error) =>{
      readProjectiles().then((projectilelist) => { this.setState({ProjectileList: projectilelist});
      }).catch((error) => {this.setState({ProjectileList : []});});
    });
  }

  reloadData = () =>{
    readTanks().then((tanklist) => { this.setState({TankList: tanklist});
    }).catch((error) => {this.setState({TankList: []});});
    readProjectiles().then((projectilelist) => { this.setState({ProjectileList: projectilelist});
    }).catch((error) => {this.setState({ProjectileList : []});});
  }

  setTank = () => {
    this.CheckIfTank = true
    this.CheckIfProjectile = false
    this.reloadDataServer()
    this.setState({Check: true})
  }
  setProjectile = () => {
    this.CheckIfProjectile = true 
    this.CheckIfTank = false
    this.reloadDataServer()
    this.setState({Check:false})
  }

  showItemAdd = () => {
    if (this.CheckIfTank == true)
      this.setState({AddCheckTank: true})
    if (this.CheckIfProjectile == true)
      this.setState({AddCheckProjectile:true})
  }
  showItemUpdate = () => {
    if (this.CheckIfTank == true)
      this.setState({UpdateCheckTank:true})
    if (this.CheckIfProjectile == true)
      this.setState({UpdateCheckProjeclite:true})
  }
  showItemFilter = () => {
    if (this.CheckIfTank == true)
      this.setState({FilterCheckTank: true})
    if (this.CheckIfProjectile == true)
      this.setState({FilterCheckProjectile: true})
  }

  render() {
    return (
      <View style={styles.container}>
        <View style = {styles.containerMaintBtn}>
          <Button
            color = "#00A5C9"
            title = "Tank"
            onPress = {this.setTank}
          />
          <Button 
            color = "#00A5C9"
            title = "Projectile"
            onPress = {this.setProjectile}          
          />
        </View>

        <View style= {styles.containerBtn}>
          <Button
              color = "#20AD02"
              title = "Add"
              onPress = {this.showItemAdd}
          />
          <Button 
              color = "#20AD02"
              title = "Filter"
              onPress = {this.showItemFilter}          
          />
          <Button 
              color = "#20AD02"
              title = "Update"
              onPress = {this.showItemUpdate}          
          />
        </View>
        
        <View style={styles.containerList}> 
          <FlatList
              data = {this.state.Check == true ? this.state.TankList : this.state.ProjectileList}
              renderItem = {({item,index}) =>{
                  if (this.state.Check == true)
                    return (
                      <TankItemList item={item} index={index}>
                      </TankItemList>
                    );
                  else
                  return (
                    <ProjectileItemList item={item} index={index}>
                    </ProjectileItemList>
                  );
                }}
                keyExtractor = {item => item.Id.toString()}
          />
        </View> 
               
        <Modal 
            visible = { this.state.AddCheckTank }
            onRequestClose = {() => console.warn("da")}>
            <View style= {styles.ModalAddTank}>
              <Text style={styles.filterText}>Add Tank</Text>
              <TextInput 
                placeholder = "Name" 
                onChangeText = {(text) => this.setState({TankName: text})}/>
              <TextInput 
                placeholder = "Weight"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankWeight: parseInt(text)})}/>
              <TextInput 
                placeholder = "Gun"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankGun: parseInt(text)})}/>
              <TextInput 
                placeholder = "Country"
                onChangeText = {(text) => this.setState({TankCountry: text})}/>
              <TextInput 
                placeholder = "Number"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankNumber: parseInt(text)})}/>
              <Button 
                title = "Add"
                onPress = {() =>{
                  const tankItem = {
                    Id: Math.floor(Date.now() / 1000),
                    Name: this.state.TankName,
                    Weight: this.state.TankWeight,
                    Gun: this.state.TankGun,
                    Country: this.state.TankCountry,
                    Number: this.state.TankNumber,
                    type: null
                  };
                  insertTank(tankItem).then().catch((error) => {Alert.alert('insert' + error);});
                  addTank(tankItem).then();
                  this.setState({AddCheckTank:false});
                  this.resetData();
                }}/>
              <Button 
                title = "Close"
                onPress = {() =>{
                  this.setState({AddCheckTank:false
                  })}}/>
            </View>
        </Modal>

        <Modal 
            visible = { this.state.UpdateCheckTank }
            onRequestClose = {() => console.warn("da")}>
            <View style= {styles.ModalAddTank}>
              <Text style={styles.filterText}>Update Tank</Text>
              <TextInput 
                placeholder = "Name"
                onChangeText = {(text) => this.setState({TankName: text})}/>
              <TextInput 
                placeholder = "Weight"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankWeight: text})}/>
              <TextInput 
                placeholder = "Gun"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankGun: text})}/>
              <TextInput 
                placeholder = "Country"
                onChangeText = {(text) => this.setState({TankCountry: text})}/>
              <TextInput 
                placeholder = "Number"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankNumber: text})}/>
              <Button 
                title = "Update"
                onPress = {() =>{
                  const tankItem = {
                      Name: this.state.TankName,
                      Country: this.state.TankCountry,
                      Gun: this.state.TankGun,
                      Weight: this.state.TankWeight,
                      Number: this.state.TankNumber
                    };
                  updateTankS(this.state.getTanks,tankItem).then((response) =>{
                    if (response === "OK"){
                      updateTank(tankItem).then().catch((error) => {
                        Alert.alert('Update error ' + error);
                      });
                    }
                    else
                      Alert.alert("No connection");
                  }).catch((error) => {Alert.alert("No connection");})
                  
                  this.setState({UpdateCheckTank:false
                  });
                  this.resetData();
                  }}/>
              <Button 
                title = "Close"
                onPress = {() =>{
                  this.setState({UpdateCheckTank:false})
                  }}/>
            </View>
        </Modal>

        <Modal 
            visible = { this.state.FilterCheckTank }
            onRequestClose = {() => console.warn("da")}>
            <View style= {styles.ModalAddTank}>
              <Text style= {styles.filterText}> Filter Tank </Text>
              <TextInput 
                placeholder = "Country"
                onChangeText = {(text) => this.setState({TankFilterCountry: text})}/>
              <TextInput 
                placeholder = "Gun"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankFilterGun: text})}/>
              <TextInput 
                placeholder = "Weight"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankFilterWeight: text})}/>
              <TextInput 
                placeholder = "Number"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({TankFilterNumber: text})}/>
              <Button 
                title = "Filter"
                onPress = {() =>{
                  let str = "";
                  if (this.state.TankFilterCountry != '')
                  {  
                    str += "Country = " + "'" + this.state.TankFilterCountry + "'";
                  }
                  if (this.state.TankFilterGun != '')
                  {
                    if (this.state.TankFilterCountry != '')
                      str += " AND Gun = " + this.state.TankFilterGun;
                    else
                      str += "Gun = " + this.state.TankFilterGun;
                  }
                  if (this.state.TankFilterWeight != '')
                  {
                    if (this.state.TankFilterCountry != '' || this.state.TankFilterGun != '')
                      str += " AND " + this.state.TankFilterWeight + " <= Weight";
                    else
                      str += this.state.TankFilterWeight + " <= Weight";
                  }
                  if (this.state.TankFilterNumber != '')
                  {
                    if (this.state.TankFilterCountry != '' || this.state.TankFilterGun != '' || this.state.TankFilterWeight != '')
                      str += " AND " + this.state.TankFilterNumber + " <= Number";
                    else
                      str += this.state.TankFilterNumber + " <= Number";
                  }
                  filterTank(str).then(tankfilterlist =>{
                      this.setState({TankList: tankfilterlist});
                    }).catch(error =>{Alert.alert(error); this.setState({TankList: [] });
                  });
                  this.setState({FilterCheckTank:false
                  });
                  this.resetData();
                  }}/>
              <Button 
                title = "Close"
                onPress = {() =>{
                  this.setState({FilterCheckTank:false
                  })}}/>
            </View>
        </Modal>

        <Modal 
            visible = { this.state.AddCheckProjectile }
            onRequestClose = {() => console.warn('dap')}>
            <View style= {styles.ModalAddProj}>
              <Text style={styles.filterText}>Add Projectile</Text>
              <TextInput 
                placeholder = "Name"
                onChangeText = {(text) => this.setState({ProjectileName: text})}/>
              <TextInput 
                placeholder = "Type"
                onChangeText = {(text) => this.setState({ProjectileType: text})}/>
              <TextInput 
                placeholder = "Caliber"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({ProjectileCaliber: parseInt(text)})}/>
              <TextInput 
                placeholder = "Number"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({ProjectileNumber: parseInt(text)})}/>
              <Button 
                title = "Add"
                onPress = {() =>{
                  const projectileItem = {
                    Id: Math.floor(Date.now() / 1000),
                    name:this.state.ProjectileName,
                    typep: this.state.ProjectileType,
                    caliber: this.state.ProjectileCaliber,
                    number: this.state.ProjectileNumber,
                    type: null
                  };
                  insertProjectile(projectileItem).then().catch((error) => {Alert.alert('insert error ' + error);});
                  addProjectile(projectileItem).then();
                  this.setState({AddCheckProjectile:false});
                  this.resetData();
                  }}/>
              <Button 
                title = "Close"
                onPress = {() =>{
                  this.setState({AddCheckProjectile:false});
                  this.resetData();
                  }}/>
            </View>
        </Modal>

        <Modal 
            visible = { this.state.UpdateCheckProjeclite }
            onRequestClose = {() => console.warn('dap')}>
            <View style= {styles.ModalAddProj}>
              <Text style={styles.filterText}>Update Projectile</Text>
              <TextInput 
                placeholder = "Name"
                onChangeText = {(text) => this.setState({ProjectileName: text})}/>
              <TextInput 
                placeholder = "Type"
                onChangeText = {(text) => this.setState({ProjectileType: text})}/>
              <TextInput 
                placeholder = "Caliber"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({ProjectileCaliber: text})}/>
              <TextInput 
                placeholder = "Number"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({ProjectileNumber: text})}/>
              <Button 
                title = "Update"
                onPress = {() =>{
                  const projectileItem = {
                      name:this.state.ProjectileName,
                      typep: this.state.ProjectileType,
                      caliber: this.state.ProjectileCaliber,
                      number: this.state.ProjectileNumber,
                  };
                  updateProjectileS(this.state.getProjectiles ,projectileItem).then((response) =>{
                    Alert.alert(response);
                    if (response === "OK"){
                      updateProjectile(projectileItem).then().catch((error) =>{
                        Alert.alert('Update error ' + error);
                      });
                    }else
                    Alert.alert("No connection");
                  }).catch((error) =>{Alert.alert(error);})

                  this.setState({UpdateCheckProjeclite:false})
                }}/>
              <Button 
                title = "Close"
                onPress = {() =>{
                  this.setState({UpdateCheckProjeclite:false});
                  this.resetData();
                  }}/>
            </View>
        </Modal>

         <Modal 
            visible = { this.state.FilterCheckProjectile }
            onRequestClose = {() => console.warn('dap')}>
            <View style= {styles.ModalAddProj}>
            <Text style= {styles.filterText}> Filter Projectile </Text>
              <TextInput 
                placeholder = "Type"
                onChangeText = {(text) => this.setState({ProjectileFilterType: text})}/>
              <TextInput 
                placeholder = "Caliber"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({ProjectileFilterCaliber: text})}/>
              <TextInput 
                placeholder = "Number"
                keyboardType = "numeric"
                onChangeText = {(text) => this.setState({ProjectileFilterNumber: text})}/>
              <Button 
                title = "Filter"
                onPress = {() =>{
                  let str = "";
                  if (this.state.ProjectileFilterType != '')
                    str += " typep = " + "'" + this.state.ProjectileFilterType + "'";
                  if (this.state.ProjectileFilterCaliber != '')
                  {
                    if (this.state.ProjectileFilterType != '')
                      str += " AND caliber = " + this.state.ProjectileFilterCaliber;
                    else
                      str += "caliber = " + this.state.ProjectileFilterCaliber;
                  }
                  if (this.state.ProjectileFilterNumber != '')
                  {
                    if (this.state.ProjectileFilterType != '' || this.state.ProjectileFilterCaliber != '')
                      str += " AND " + this.state.ProjectileFilterNumber + " <= number ";
                    else
                      str += this.state.ProjectileFilterNumber + " <= number ";
                  }
                  filterProjectile(str).then(projectilefilterlist =>{
                      this.setState({ProjectileList: projectilefilterlist});
                    }).catch(error =>{Alert.alert(error); this.setState({ProjectileList: [] });
                  });
                  this.setState({FilterCheckProjectile:false
                  });
                  this.resetData();
                  }}/>
              <Button 
                title = "Close"
                onPress = {() =>{
                  this.setState({FilterCheckProjectile:false
                  })}}/>
            </View>
        </Modal>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex:1,
    backgroundColor: '#525252',
  },
  containerMaintBtn: {
    marginTop: 10,
    flexDirection: 'row',
    justifyContent: 'center',
  },
  containerBtn: {
    marginTop: 10,
    flexDirection: 'row',
    justifyContent: 'center',
  },
  containerList:{
    marginTop:20,
    flex:1,
    flexDirection: 'column'
  },
  ModalAddTank:{
    flex:1,
    width:320,
    justifyContent: 'center',
    backgroundColor: "#aaa"
  },
  ModalAddProj:{
    flex:1,
    width:320,
    justifyContent: 'center',
    backgroundColor: "#aaa"
  },
  filterText:{
    alignSelf: "center",
    color: "#000000",
    textDecorationStyle: "double",
    fontStyle: "italic",
    fontSize: 30,
  },
  tankItemList:{
    justifyContent: 'center',
    color: "#FFFFFF",
    textDecorationStyle: "double",
    fontStyle: "italic",
    fontSize: 40
  },
  textlist:{
    marginLeft: 20,
    fontSize: 16,
    color:"#000000",
    marginTop:10
  },
  textcontent:{
    marginLeft: 2,
    color:"#000000",
    marginTop:10
  }
});
