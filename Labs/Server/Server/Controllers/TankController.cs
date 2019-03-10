using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Server.Models;
using Server.Persistence;
using System.Collections;
using Newtonsoft.Json;

namespace Server.Controllers
{
    public class TankController : ApiController
    {
        // GET: api/Tank
        public List<Tank> Get()
        {
            TankPersistence tankPersistence = new TankPersistence();
            return tankPersistence.GetTanks();
        }

        // GET: api/Tank/5
        public Tank Get(int id)
        {
            TankPersistence tankPersistence = new TankPersistence();
            return tankPersistence.GetTank(id);
        }

        // POST: api/Tank
        public HttpResponseMessage Post(Tank value)
        {
            try
            {
                TankPersistence tankPersistence = new TankPersistence();
                tankPersistence.SaveTank(value);
            }
            catch (Exception)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }
               
            HttpResponseMessage httpResponseMessage = Request.CreateResponse(HttpStatusCode.Created);
            httpResponseMessage.Headers.Location = new Uri(Request.RequestUri, String.Format("tank/{0}",value.Id));
            return httpResponseMessage;
        }

        // PUT: api/Tank/5
        public HttpResponseMessage Put(int id, Tank value)
        {
            try
            {
                TankPersistence tankPersistence = new TankPersistence();
                tankPersistence.UpdateTank(id, value);
            }
            catch (Exception)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }
            return Request.CreateResponse(HttpStatusCode.OK);
        }

        // DELETE: api/Tank/5
        public HttpResponseMessage Delete(int id)
        {
            TankPersistence tankPersistence = new TankPersistence();
            bool Exists = tankPersistence.DeleteTank(id);
            
            if (Exists)
                return Request.CreateResponse(HttpStatusCode.OK);
            else
                return Request.CreateResponse(HttpStatusCode.NotFound);
        }
    }
}
