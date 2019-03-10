using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Server.Models;
using Server.Persistence;
using System.Collections;

namespace Server.Controllers
{
    public class ProjectileController : ApiController
    {
        // GET: api/Projectile
        public ArrayList Get()
        {
            ProjectilePersistence projectilePersistence = new ProjectilePersistence();
            return projectilePersistence.GetProjectiles();
        }

        // GET: api/Projectile/5
        public Projectile Get(int id)
        {
            ProjectilePersistence projectilePersistence = new ProjectilePersistence();
            return projectilePersistence.GetProjectile(id);
        }

        // POST: api/Projectile
        public HttpResponseMessage Post(Projectile value)
        {
            try
            {
                ProjectilePersistence projectilePersistence = new ProjectilePersistence();
                projectilePersistence.SaveProjectile(value);
            }
            catch (Exception)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }

            HttpResponseMessage httpResponseMessage = Request.CreateResponse(HttpStatusCode.Created);
            httpResponseMessage.Headers.Location = new Uri(Request.RequestUri, String.Format("projectile/{0}", value.Id));
            return httpResponseMessage;
        }

        // PUT: api/Projectile/5
        public HttpResponseMessage Put(int id, Projectile value)
        {
            try
            {
                ProjectilePersistence projectilePersistence = new ProjectilePersistence();
                projectilePersistence.UpdateProjectile(id, value);
            }
            catch (Exception)
            {
                return Request.CreateResponse(HttpStatusCode.BadRequest);
            }
            return Request.CreateResponse(HttpStatusCode.OK);
        }

        // DELETE: api/Projectile/5
        public HttpResponseMessage Delete(int id)
        {
            ProjectilePersistence projectilePersistence = new ProjectilePersistence();
            bool Exists = projectilePersistence.DeleteProjectile(id);

            if (Exists)
                return Request.CreateResponse(HttpStatusCode.OK);
            else
                return Request.CreateResponse(HttpStatusCode.NotFound);
        }
    }
}
