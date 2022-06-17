package com.best.onstyle.service;

import com.best.onstyle.domain.best.Best;
import com.best.onstyle.domain.best.BestRepository;
import com.best.onstyle.domain.itemInfo.ItemInfo;
import com.best.onstyle.domain.itemInfo.ItemInfoRepository;
import com.best.onstyle.domain.price.Price;
import com.best.onstyle.domain.price.PriceRepository;

import com.best.onstyle.domain.reply.Reply;
import com.best.onstyle.domain.reply.ReplyRepository;
import com.best.onstyle.dto.BestTop100ResponseDto;
import com.best.onstyle.dto.ItemResponseDto;
import com.best.onstyle.dto.ReplyResponseDto;
import com.best.onstyle.dto.vo.BasicItemInfoVO;
import lombok.RequiredArgsConstructor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import static org.springframework.http.HttpHeaders.USER_AGENT;

@RequiredArgsConstructor
@Service
public class ItemInfoService {

    private final BestRepository bestRepository;
    private final ItemInfoRepository itemInfoRepository;
    private final PriceRepository priceRepository;

    private final ReplyRepository replyRepository;
    private final String BASE_URL = "https://display.cjonstyle.com";

    @Transactional
    public ItemResponseDto findItemByItemInfoId(String itemInfoId){
        // item 세부정보 GET
        ItemInfo item = itemInfoRepository.findByItemInfoId(itemInfoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        Date before = new Date();
        before = new Date(before.getTime() + (1000 * 60 * 60 * 10 * -1));

        List<Best> itemRankingList = bestRepository.findAllByItemInfo(item);
        Optional<Best> todayUpdate = bestRepository.findByItemInfoAndCurrentUpdateBetween(item, before, new Date());

        boolean isCurrentUpdate = itemRankingList.size() == 1 && todayUpdate.isPresent() ? true : false;
        return new ItemResponseDto(item, isCurrentUpdate);
    }
    @Transactional
    public boolean saveItemInfos() {
        // API 데이터 파싱 후 DB에 저장하는 로직
        String bestItems = getItemBestInfoFromOpenApi();
        return parseBestInfoAndSaveItemInfos(bestItems);
    }

    @Transactional
    public Long updateItemInfoLikeCnt(String itemInfoId){
        // 아이템 좋아요 증가
        ItemInfo item = itemInfoRepository.findByItemInfoId(itemInfoId)
                .orElseThrow(()->new IllegalArgumentException("해당 상품이 없습니다. id=" + itemInfoId));

        item.updateLikeCnt();
        return itemInfoRepository.save(item).getLikeCnt();
    }

    @Transactional
    public List<BestTop100ResponseDto> findItemByTitleContaining(String name){
        // 검색기능
        List<ItemInfo> itemInfoList = itemInfoRepository.findAllByItemNameContaining(name);
        return itemInfoList.stream().map(item -> getBestTop100ResponseDtoFromBest(item))
                .collect(Collectors.toList());
    }

    private BestTop100ResponseDto getBestTop100ResponseDtoFromBest(ItemInfo item) {
        Optional<Reply> likeTopReply = replyRepository.findTopByItemInfoOrderByLikeCntDesc(item);
        Optional<Reply> hateTopReply = replyRepository.findTopByItemInfoOrderByHateCntDesc(item);

        ReplyResponseDto likeReplyResponseDto = likeTopReply.filter(r -> r.getLikeCnt() >= 1)
                .map(ReplyResponseDto::new)
                .orElse(null);
        ReplyResponseDto hateReplyResponseDto = hateTopReply.filter(h -> h.getHateCnt() >= 1)
                .map(ReplyResponseDto::new)
                .orElse(null);
        return new BestTop100ResponseDto(item, likeReplyResponseDto, hateReplyResponseDto);
    }

    private String getItemBestInfoFromOpenApi() {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("c/rest/category/getTop100ItemList")
                .queryParam("type", "P")
                .build();

        return connectOpenApiAndGetInfoStr(uriComponents.toString());
    }

    private String getItemBasicInfoFromOpenApi(String itemCd) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/c/rest/item/{itemCd}/itemInfo.json")
                .queryParam("channelCode", 50001002)
                .queryParam("isEmployee", false)
                .queryParam("isMyzone", false)
                .queryParam("isSimple", false)
                .queryParam("isSearch", false)
                .buildAndExpand(itemCd);

        return connectOpenApiAndGetInfoStr(uriComponents.toString());
    }

    private String connectOpenApiAndGetInfoStr(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-type", "application/json");

            con.setDoOutput(true);

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            while (br.ready()) {
                sb.append(br.readLine());
            }
            con.disconnect();

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean parseBestInfoAndSaveItemInfos(String requestData) {
        try {
            JSONObject data = (JSONObject) new JSONParser().parse(requestData);
            JSONObject result = (JSONObject) data.get("result");
            JSONArray cateTop100ItemTupleList = (JSONArray) result.get("cateTop100ItemTupleList");
            // 기본 TOP 100 정보 파싱
            for (Object itemInfo : cateTop100ItemTupleList) {

                JSONObject itemInfoJson = (JSONObject) itemInfo;
                String itemCode = (String) itemInfoJson.get("itemCd");

                JSONObject rmItempriceInfo = (JSONObject) itemInfoJson.get("rmItempriceInfo");

                // 카테고리 데이터를 얻어오기 위한 추가 요청 및 파싱
                String itemBasicInfo = getItemBasicInfoFromOpenApi(itemCode);
                BasicItemInfoVO basicItemInfoVO = parseItemBasicInfo(itemBasicInfo);
                
                Optional<ItemInfo> findByItemInfo = itemInfoRepository.findByItemInfoId(itemCode);

                // API JSON을 파싱하여 상품이 있다면 update, 없을 경우 insert
                Optional<ItemInfo> newItem = Optional.ofNullable(findByItemInfo.map(item -> {
                    item.update((String) itemInfoJson.get("itemCd"),
                            (String) itemInfoJson.get("itemNm"),
                            (String) itemInfoJson.get("brandNm"),
                            (String) itemInfoJson.get("itemImgUrl"),
                            basicItemInfoVO,
                            new Date()
                            );
                    return item;
                }).orElseGet(() -> {
                    return ItemInfo.builder()
                            .itemInfoId((String) itemInfoJson.get("itemCd"))
                            .itemImgUrl((String) itemInfoJson.get("itemImgUrl"))
                            .itemName((String) itemInfoJson.get("itemNm"))
                            .brandName((String) itemInfoJson.get("brandNm"))
                            .basicItemInfoVO(basicItemInfoVO)
                            .currentUpdate(new Date())
                            .build();
                }));

                //가격 정보 insert
                Price price = Price.builder()
                        .itemInfo(newItem.get())
                        .price(Long.valueOf(String.valueOf(rmItempriceInfo.get("salePrice"))))
                        .discountRate(Long.valueOf(String.valueOf(rmItempriceInfo.get("discountRate"))))
                        .customerPrice(Long.valueOf(String.valueOf(rmItempriceInfo.get("customerPrice"))))
                        .currentUpdate(new Date()).build();

                //랭킹 정보 insert
                Best best = Best.builder()
                        .itemInfo(newItem.get())
                        .ranking(Long.valueOf(String.valueOf(itemInfoJson.get("ranking"))))
                        .currentUpdate(new Date()).build();

                itemInfoRepository.save(newItem.get());
                bestRepository.save(best);
                priceRepository.save(price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private BasicItemInfoVO parseItemBasicInfo(String requestData) {
        try {
            JSONObject data = (JSONObject) new JSONParser().parse(requestData);
            JSONObject result = (JSONObject) data.get("result");

            JSONObject cateInfo = (JSONObject) result.get("cateInfo");
            JSONObject detailInfo = (JSONObject) result.get("detailInfo");
            JSONObject itemPrice = (JSONObject) detailInfo.get("itemPrice");

            String largeCategoryName = (String) cateInfo.get("largeCategoryName");
            String middleCategoryName = (String) cateInfo.get("middleCategoryName");
            String categoryName = (String) cateInfo.get("categoryName");

            BasicItemInfoVO basicItemInfoVO = new BasicItemInfoVO(largeCategoryName, middleCategoryName, categoryName);
            return basicItemInfoVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
