def build_test(){
  echo 'maven build & test...'
  sh "./mvnw clean package -e" 
}

return this
