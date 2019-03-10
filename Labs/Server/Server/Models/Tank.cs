using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Server.Models
{
    public class Tank
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int Weight { get; set; }
        public int Gun { get; set; }
        public string Country { get; set; }
        public int Number { get; set; }

        public Tank(int Id, string Name, int Weight, int Gun, string Country, int Number)
        {
            this.Id = Id;
            this.Name = Name;
            this.Weight = Weight;
            this.Gun = Gun;
            this.Country = Country;
            this.Number = Number;
        }
        public Tank() { }
    }
}