using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc;

namespace WatchDogApi.Controllers
{
    [Route("api/v1/[controller]")]
    [ApiController]
    public class AttackController : ControllerBase
    {
        private static readonly object Mutex = new object();

        private static bool _shouldAttack;
        
        [HttpGet]
        public ActionResult<IDictionary<long, string>> Get([FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "000")
            {
                return Forbid();
            }

            lock (Mutex)
            {
                var temp = _shouldAttack;
                _shouldAttack = false;
                return Ok(temp);
            }
        }
        
        [HttpPost]
        public ActionResult Post([FromBody] bool setAttack, [FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "000")
            {
                return Forbid();
            }
            
            lock (Mutex)
            {
                _shouldAttack = setAttack;
                return Ok();
            }
        }
    }
}