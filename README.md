# mileage
트리플여행자 클럽 마일리지 서비스

# 실행방법
1. 터미널을 켜서 특정 directory에 프로젝트를 clone합니다.
``` 
cd 디렉터리
git clone https://github.com/hjy5421/mileage.git
```

2. Mysql 을 로컬에 다운받습니다.

3. 프로젝트의 src/main/resources/application.properties 내의 DB 설정을 로컬 Mysql 환경에 맞게 변경해줍니다.
```
spring.datasource.url=${db url}
spring.datasource.username=${username}
spring.datasource.password=${password}
```

4. 터미널에서 아래 명령어를 실행해줍니다.
```
./gradlew build
cd build/libs 
java -jar mileage-0.0.1-SNAPSHOT.jar
```

5. http://localhost:8080 으로 요청을 보냅니다.


# API

## POST /events
- reqest

| 필드 | 타입 | 필수 여부 | 
| ------ | ------ | ------ |
| type | String | O |
| action | String | O (ADD, MOD, DELETE만 허용) |
| reviewId | String | O |
| content | String | X (action=DELETE인 경우), O (그 외 action인 경우) |
| attachedPhotoIds | List | X (action=DELETE인 경우), O (그 외 action인 경우) |
| userId | String | O |
| placeId | String | O |


```
{
    "type":"REVIEW",
    "action":"ADD",
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772,
    "content":"좋아요!",
    "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
    "userId":"3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId":"2e4baf1c-5acb-4efb-a1af-eddada31bb00f"
}
```

- response

| 필드 | 타입 | 필수 여부 | 
| ------ | ------ | ------ |
| resultCode | String | O (SUCCESS, CLIENTERROR, SERVERERROR만 존재) |
| message | String | O |
| data | Object | X |


```
{
    "resultCode":"SUCCESS",
    "message":"OK",
    "data": null
}
```

## GET /points
- reqest

| 필드 | 타입 | 필수 여부 | 
| ------ | ------ | ------ |
| userId | String | O |

```
http://localhost:8080/points?userId=3ede0ef2-92b7-4817-a5f3-0c575361f745
```

- response

| 필드 | 타입 | 필수 여부 | 
| ------ | ------ | ------ |
| resultCode | String | O (SUCCESS, CLIENTERROR, SERVERERROR만 존재) |
| message | String | O |
| data | Object | O (SUCCESS인 경우), X (그 외의 경우) |

```
{
    "resultCode": "SUCCESS",
    "message": "OK",
    "data": {
        "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
        "userPoint": 0
    }
}
```

# DDL
- photo

```
    create table photo (
       photo_id varchar(255) not null,
        review_id varchar(255) not null,
        primary key (photo_id)
    ) engine=InnoDB
```

- place

```
    create table place (
       place_id varchar(255) not null,
        primary key (place_id)
    ) engine=InnoDB
```

- point history

```
    create table point_history (
       id bigint not null auto_increment,
        change_point integer not null,
        review_id varchar(255) not null,
        user_id varchar(255) not null,
        primary key (id)
    ) engine=InnoDB
    
    create index IDXsx1exis4ryuqjvu8n6rgccd5m on point_history (user_id)
```

- review

```
    create table review (
       review_id varchar(255) not null,
        content longtext not null,
        create_date datetime(6),
        modify_date datetime(6),
        place_id varchar(255) not null,
        user_id varchar(255) not null,
        primary key (review_id)
    ) engine=InnoDB
    
    create index IDXatymnyfpvy2nhuaqmrds49qrp on review (user_id, place_id)
```

- user

```
    create table user (
       user_id varchar(255) not null,
        user_point integer default 0 not null,
        primary key (user_id)
    ) engine=InnoDB
```


