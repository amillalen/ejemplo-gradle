def build_test(){
  echo 'maven build & test...'
  sh "./mvnw clean package -e" 
}

def run(){
  echo "run..."
  sh "./mvnw spring-boot:run  > /tmp/mscovid.log 2>&1 &"
}
return this
