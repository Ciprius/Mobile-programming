using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Server.Models
{
    public class Projectile
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Type { get; set; }
        public int Caliber { get; set; }
        public int Number { get; set; }

        public Projectile(int id, string name, string type, int caliber, int number)
        {
            Id = id;
            Name = name;
            Type = type;
            Caliber = caliber;
            Number = number;
        }
        public Projectile() { }
    }
}