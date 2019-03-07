using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace WatchDogApi.Controllers
{
    [Route("api/v1/[controller]")]
    [ApiController]
    public class HealthValuesController : ControllerBase
    {
        [HttpGet]
        public ActionResult<IDictionary<long, string>> Get([FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "555")
            {
                return Forbid();
            }
            
            return HeartRateStorage.GetHeartRateValues();
        }

        [HttpPost]
        public ActionResult Post([FromBody] string value, [FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "666")
            {
                return Forbid();
            }
            
            HeartRateStorage.AddNewValue(value);
            return Ok();
        }

        [HttpDelete]
        public ActionResult Delete([FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "12345")
            {
                return Forbid();
            }
            
            HeartRateStorage.ClearStorage();
            return Ok();
        }
    }
}