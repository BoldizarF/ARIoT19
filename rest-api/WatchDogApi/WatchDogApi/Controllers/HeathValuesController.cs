using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using WatchDogApi.Storage;

namespace WatchDogApi.Controllers
{
    [Route("api/v1/[controller]")]
    [ApiController]
    public class HealthValuesController : ControllerBase
    {
        [HttpGet]
        public ActionResult<IDictionary<long, string>> Get([FromQuery(Name = "apikey")] string apikey)
        {
            if (!ActiveClientStorage.IsClientAccepted(apikey))
            {
                return Forbid();
            }
            
            return HealthValueStorage.GetHealthValues();
        }

        [HttpPost]
        public ActionResult Post([FromBody] string value, [FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "666")
            {
                return Forbid();
            }
            
            HealthValueStorage.AddNewValue(value);
            return Ok();
        }

        [HttpDelete]
        public ActionResult Delete([FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "12345")
            {
                return Forbid();
            }
            
            HealthValueStorage.ClearStorage();
            return Ok();
        }
    }
}