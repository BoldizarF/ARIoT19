using System.Security.Cryptography;
using System.Text;
using Microsoft.AspNetCore.Mvc;
using WatchDogApi.Storage;

namespace WatchDogApi.Controllers
{
    [Route("api/v1/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        private const string HashedPassKey = "939CFA250257869CC6B7B11EEAF8C2261F6EE051C9A9A09753AB3A65DC78CFEA";
        
        [HttpPost]
        public ActionResult Post([FromBody] string passKey)
        {
            if (ComputeSha256Hash(passKey) == HashedPassKey)
            {
                Ok("");
            }

            var apiKey = ActiveClientStorage.AddClient();
            return Ok(apiKey);
        }
        
        
        [HttpDelete]
        public ActionResult Delete([FromQuery(Name = "apikey")] string apikey)
        {
            if (apikey != "12345")
            {
                return Forbid();
            }
            
            ActiveClientStorage.Clear();
            return Ok();
        }
        
        private static string ComputeSha256Hash(string rawData)  
        {  
            using (var sha256Hash = SHA256.Create())  
            {  
                var bytes = sha256Hash.ComputeHash(Encoding.UTF8.GetBytes(rawData));  
                var builder = new StringBuilder();  
                foreach (var t in bytes)
                {
                    builder.Append(t.ToString("x2"));
                }  
                return builder.ToString().ToUpper();  
            }  
        }  
    }
}