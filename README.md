# best100-backend 

Front end: [FE best100 Repository](https://github.com/choieunii/best100-frontend)

```
Front-end : Next.js, TypeScript, Nivo, CSS IN JS
Back-end : Springboot, JPA, MySQL
```

### 개발 기간 
2022-06-08 ~ 2022-06-17

### 1. 초기 페이지 베스트 top 100
```
 GET : /best/top100 
```

* param : sort = ?, filter = ?
* sort - likeCnt, replyCnt       
* filter - increaseRank, decreaseRank, sale
* sort param이 없을 경우 기본 랭킹 순 정렬

### response
```jsx
{
        "id": 2,
        "itemInfoId": "2002813161",
        "ranking": 2,
        "brandName": "비비안 스킨핏(VIVIEN SKIN FIT)",
        "itemName": "비비안 스킨핏 썸머 프리핏 BLACK LABEL",
        "itemImgUrl": "//itemimage.cjonstyle.net/goods_images/20/161/2002813161L.jpg",
        "largeCateName": "TV쇼핑 패션의류/언더웨어",
        "middleCateName": "언더웨어",
        "cateName": "브라/팬티세트",
        "likeCnt": 0,
        "likeTopReply": null,
        "hateTopReply": null,
        "price": []
    },
```

### 2. 상품 세부정보 필터링
```
GET : /best/category
```
* param : categoryType = ?, categoryName = ?
* categoryType - largeCategory, middleCategory, category, brandName      
* categoryName - 선택한 카테고리 이름

### 3. 5일치 같은 랭킹 필터링
```
GET : /best/ranking
```
* param : ranking = ?

### 4. 최근 5일치 BEST 5 필터링
```
GET : /best/rankings/top5
```
### 5. 상품 검색
```
GET : /item/search
```
* param : itemName = ?

### 6. 상품 별 랭킹 변동 사항 GET
```
GET : /best/rankings/{ItemInfoId}
```
### response
```jsx
[
    {
        "id": 176,
        "ranking": 76,
        "currentUpdate": "2022-06-12T06:34:41.000+0000"
    },
    {
        "id": 9,
        "ranking": 9,
        "currentUpdate": "2022-06-11T16:23:24.000+0000"
    }
]
```

### 7. DB 데이터 저장
* 외부 API 호출 후 필요한 데이터 저장
```
GET : /item/data
```
### 8. 상품 코드에 따라 아이템 정보 GET
```
GET : /item/{itemInfoId}
```
### response
```jsx
{
    "id": 9,
    "itemInfoId": "2002319724",
    "ranking": 76,
    "brandName": "미세스문",
    "itemName": "[미세스문]킹 목화솜 풍기인견 침구세트",
    "itemImgUrl": "//itemimage.cjonstyle.net/goods_images/20/724/2002319724L.jpg",
    "largeCateName": "TV쇼핑 가구/침구",
    "middleCateName": "침구",
    "cateName": "이불/침구세트",
    "likeCnt": null,
    "price": 209000,
    "currentUpdateBest": false
}
```

### 9.  좋아요
```
POST : /item/like/{itemInfoId}
```
### 10. 상품 최근 가격 리스트 출력
```
GET : /best/prices/{itemInfoId}
```
### response
```jsx
[
    {
        "id": 176,
        "price": 209000,
        "currentUpdate": "2022-06-12T06:42:08.000+0000"
    },
    {
        "id": 76,
        "price": 209000,
        "currentUpdate": "2022-06-12T06:34:41.000+0000"
    }
]
```

### 11. 댓글 리스트 출력 
* 기본 최신순 정렬, 사이즈 5
```
GET : /reply/{itemInfoId}
```
* param : sort = ?, page = ?
### response
```jsx
[
    {
        "id": 1,
        "content": "첫번째 댓글",
        "likeCnt": 0,
        "hateCnt": 0,
        "currentUpdate": "2022-06-12T07:29:29.000+0000"
    }
]
```

### 12. BEST 좋아요, 싫어요 GET 
* 댓글의 좋아요, 싫어요 수가 1 이상 일때만 BEST로 등록
```
GET : /reply/top/{itemInfoId} 
```
### response
```jsx
{
    "likeTopReply": {
        "id": 1,
        "content": "첫번째 댓글",
        "likeCnt": 0,
        "hateCnt": 0,
        "currentUpdate": "2022-06-12T07:29:29.000+0000"
    },
    "hateTopReply": {
        "id": 1,
        "content": "첫번째 댓글",
        "likeCnt": 0,
        "hateCnt": 0,
        "currentUpdate": "2022-06-12T07:29:29.000+0000"
    }
}
```

### 13. 댓글 등록
```
POST : /reply 
```
### request
```
{
    "itemInfoId" : "123456",
    "content" : "새로운 댓글",
    "password" : "1234",
    "hasPassword" : true
}
```


### 14. 댓글 수정
```
PUT : /reply/{replyId}
```

* 비밀번호 불일치 시 error


### 15. 댓글 삭제
```
POST : /reply/{replyId}
```
* 비밀번호 불일치 시 error
 
### 16. 댓글 좋아요
```
POST : /reply/like/{replyId}
```
### 17. 댓글 싫어요
```
POST : /reply/hate/{replyId}
```
