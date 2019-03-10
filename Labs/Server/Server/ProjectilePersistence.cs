using Server.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Collections;

namespace Server.Persistence
{
    public class ProjectilePersistence
    {
        private SqlConnection conn = new SqlConnection(@"Data Source = DESKTOP-3DU7K2P\SQLEXPRESS; " +
                " Initial Catalog = MobileDB; Integrated Security = true;");

        public ProjectilePersistence(){}

        public ArrayList GetProjectiles()
        {
            ArrayList projectiles = new ArrayList();
            conn.Open();
            SqlCommand command = new SqlCommand("SELECT * FROM Projectiles", conn);
            SqlDataReader reader = command.ExecuteReader();

            while (reader.Read())
            {
                int Id = Convert.ToInt32(reader["Id"]);
                string Name = reader["Names"].ToString();
                string Type = reader["Tipes"].ToString();
                int Caliber = Convert.ToInt32(reader["Caliber"]);
                int Number = Convert.ToInt32(reader["Number"]);
                projectiles.Add(new Projectile(Id, Name,Type,Caliber, Number));
            }
            reader.Close();
            conn.Close();
            return projectiles;
        }

        public Projectile GetProjectile(int id)
        {
            conn.Open();
            SqlCommand command = new SqlCommand("select * from Projectiles where Id = @Id", conn);
            command.Parameters.AddWithValue("@Id", id);
            command.CommandType = System.Data.CommandType.Text;
            SqlDataReader reader = command.ExecuteReader();

            if (reader.HasRows)
            {
                reader.Read();
                int Id = Convert.ToInt32(reader["Id"]);
                string Name = reader["Names"].ToString();
                int Caliber = Convert.ToInt32(reader["Caliber"]);
                string Type = reader["Tipes"].ToString();
                int Number = Convert.ToInt32(reader["Number"]);
                reader.Close();
                conn.Close();
                return new Projectile(Id, Name, Type, Caliber, Number);
            }
            reader.Close();
            conn.Close();
            return null;
        }

        public void SaveProjectile(Projectile projectile)
        {
            conn.Open();
            SqlCommand command = new SqlCommand("insert into Projectiles(Names, Tipes, Caliber, Number) " +
                "Values(@Names, @Tipes, @Caliber, @Number)", conn);
            command.Parameters.AddWithValue("@Names", projectile.Name);
            command.Parameters.AddWithValue("@Tipes", projectile.Type);
            command.Parameters.AddWithValue("@Caliber", projectile.Caliber);
            command.Parameters.AddWithValue("@Number", projectile.Number);
            command.CommandType = System.Data.CommandType.Text;
            command.ExecuteNonQuery();
            conn.Close();
        }

        public void UpdateProjectile(int id,Projectile projectile)
        {
            conn.Open();
            using (SqlCommand command = new SqlCommand("update Projectiles set Tipes=@Tipes, Caliber=@Caliber, Number=@Number" +
                " Where Id = @Id", conn))
            {
                command.Parameters.AddWithValue("@Tipes", projectile.Type);
                command.Parameters.AddWithValue("@Caliber", projectile.Caliber);
                command.Parameters.AddWithValue("@Number", projectile.Number);
                command.Parameters.AddWithValue("@Id", id);
                command.CommandType = System.Data.CommandType.Text;
                command.ExecuteNonQuery();
            }
            conn.Close();
        }

        public bool DeleteProjectile(int id)
        {
            conn.Open();

            SqlCommand cmd = new SqlCommand("select * from Projectiles where Id = @Id", conn);
            cmd.Parameters.AddWithValue("@Id", id);
            SqlDataReader reader = cmd.ExecuteReader();

            if (reader.HasRows)
            {
                reader.Close();
                SqlCommand command = new SqlCommand("delete from Projectiles where Id =@Id", conn);
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