using Server.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;
using System.Collections;

namespace Server.Persistence
{
    public class TankPersistence
    {
        private SqlConnection conn = new SqlConnection(@"Data Source = DESKTOP-3DU7K2P\SQLEXPRESS; " +
                " Initial Catalog = MobileDB; Integrated Security = true;");

        public TankPersistence() {}

        public List<Tank> GetTanks()
        {
            List<Tank> tanks = new List<Tank>();
            conn.Open();
            SqlCommand command = new SqlCommand("SELECT * FROM Tanks", conn);
            SqlDataReader reader = command.ExecuteReader();

            while(reader.Read())
            {
                int Id = Convert.ToInt32(reader["Id"]);
                string Name = reader["Names"].ToString();
                int Weight = Convert.ToInt32(reader["Weights"]);
                int Gun = Convert.ToInt32(reader["Gun"]);
                string Country = reader["Country"].ToString();
                int Number = Convert.ToInt32(reader["Number"]);
                tanks.Add(new Tank(Id,Name,Weight,Gun,Country,Number));
            }
            conn.Close();
            reader.Close();
            return tanks;
        }

        public Tank GetTank(int id)
        {
            conn.Open();
            SqlCommand command = new SqlCommand("select * from Tanks where Id = @Id",conn);
            command.Parameters.AddWithValue("@Id", id);
            command.CommandType = System.Data.CommandType.Text;
            SqlDataReader reader = command.ExecuteReader();

            if (reader.HasRows)
            {
                reader.Read();
                int Id = Convert.ToInt32(reader["Id"]);
                string Name = reader["Names"].ToString();
                int Weight = Convert.ToInt32(reader["Weights"]);
                int Gun = Convert.ToInt32(reader["Gun"]);
                string Country = reader["Country"].ToString();
                int Number = Convert.ToInt32(reader["Number"]);
                reader.Close();
                conn.Close();
                return new Tank(Id, Name, Weight, Gun, Country, Number);
            }
            reader.Close();
            conn.Close();
            return null;
        }

        public void SaveTank(Tank tank)
        {
            conn.Open();
            SqlCommand command = new SqlCommand("insert into Tanks(Names, Weights, Gun, Country, Number) " +
                "Values(@Names,@Weights, @Gun, @Country, @Number)",conn);
            command.Parameters.AddWithValue("@Names", tank.Name);
            command.Parameters.AddWithValue("@Weights", tank.Weight);
            command.Parameters.AddWithValue("@Gun", tank.Gun);
            command.Parameters.AddWithValue("@Country", tank.Country);
            command.Parameters.AddWithValue("@Number", tank.Number);
            command.CommandType = System.Data.CommandType.Text;
            command.ExecuteNonQuery();
            conn.Close();
        }

        public void UpdateTank(int id,Tank tank)
        {
            conn.Open();
            SqlCommand command = new SqlCommand("update Tanks set Weights=@Weights, Gun=@Gun, Country=@Country, Number=@Number" +
                " Where Id = @Id", conn);
            command.Parameters.AddWithValue("@Weights",tank.Weight);
            command.Parameters.AddWithValue("@Gun", tank.Gun);
            command.Parameters.AddWithValue("@Country",tank.Country);
            command.Parameters.AddWithValue("@Number", tank.Number);
            command.Parameters.AddWithValue("@Id", id);
            command.CommandType = System.Data.CommandType.Text;
            command.ExecuteNonQuery();
            conn.Close();
        }

        public bool DeleteTank(int id)
        {
            conn.Open();
            SqlCommand cmd = new SqlCommand("select * from Tanks where Id = @Id",conn);
            cmd.Parameters.AddWithValue("@Id",id);
            SqlDataReader reader = cmd.ExecuteReader();

            if (reader.HasRows)
            {
                reader.Close();
                SqlCommand command = new SqlCommand("delete from Tanks where Id =@Id", conn);
                command.Parameters.AddWithValue("@Id", id);
                command.CommandType = System.Data.CommandType.Text;
                command.ExecuteNonQuery();
                conn.Close();
                return true;
            }
            conn.Close();
            return false;
        }
    }
}