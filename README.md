# best100-backend (정리중)
### GET : /best/top100 - 초기 페이지 베스트 top 100

requestparam - sort: likeCnt, replyCnt

sort param이 없을 경우 기본 랭킹 순 정렬

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

### GET : /best/rankings/{ItemInfoId} - 현재까지의 랭킹 변동 사항

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

### GET : /item/data - DB 데이터 

### GET : /item/{itemInfoId} - 상품 코드에 다른 아이템 정보

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

### POST : /item/like/{itemInfoId} - 아이템 좋아요

### GET : /best/prices/{itemInfoId} - 아이템 최근 가격 리스트 출력

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

### GET : /reply/{itemInfoId} → 기본 최신순 정렬 사이즈는 10

queryparameter : sort = ?

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

### GET : /reply/top/{itemInfoId} → 1이상 일때만 처리 필요

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

### POST : /reply



### PUT : /reply/{replyId}


비밀번호 불일치시


### DELETE : /reply/{replyId}

### POST : /reply/like/{replyId}

### POST : /reply/hate/{replyId}
