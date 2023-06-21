# Cucumber-market v2
기존 cucumber-market에서 Redis(cache)를 이용해 성능을 높이고 aws-ec2 배포를 시도한 프로젝트입니다.
 - redis cache를 적용한 부분은 board이며 성능테스트는 postman으로 했습니다.
 - cache에 저장이 안됐을때 250ms 였고
 ![first](https://github.com/hyeok-min/redis-cucumber/assets/123552584/3c86238e-b0f5-451e-8f29-ae0185e8118d)
- cache에 저장이 됐을때 45ms로 줄어든 모습을 볼수있었습니다.
![secound](https://github.com/hyeok-min/redis-cucumber/assets/123552584/b57c60a5-cfca-4ae2-8e25-09a9655c1544)
