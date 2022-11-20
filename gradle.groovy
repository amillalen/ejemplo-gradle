def build_test(){
  echo ' gradle build & test...'
  sh "gradle build"
}

def run(){
  echo 'run...'
  sh "nohup gradle bootRun > /tmp/mscovid.log 2>&1 &" 
}

return this
