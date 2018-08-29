package wannagohome.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.card.*;
import wannagohome.domain.file.Attachment;
import wannagohome.domain.user.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.CardService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@Api(value = "Card", description = "Card 관리 API")
public class ApiCardController {
    private static final Logger log = LoggerFactory.getLogger(ApiCardController.class);

    @Autowired
    private CardService cardService;

    @ApiOperation(value = "유저로 카드 가져오기")
    @ApiImplicitParam(name = "readCards", value = "Card 리스트", required = true,  paramType = "json")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Card> readCards(@LoginUser User user) {
        return cardService.findCardsByUser(user);
    }

    @ApiOperation(value = "카드 삭제하기")
    @ApiImplicitParam(name = "deleteCard", value = "Card", required = true,  paramType = "json")
    @DeleteMapping("/{cardId}")
    public Card deleteCard(@PathVariable Long cardId) {
        return cardService.deleteCard(cardId);
    }

    @ApiOperation(value = "Due date 존재하는 카드 가져오기")
    @ApiImplicitParam(name = "readDueCards", value = "Card 리스트", required = true,  paramType = "json")
    @GetMapping("/calendar")
    @ResponseStatus(HttpStatus.OK)
    public List<Card> readDueCards(@LoginUser User user) {
        return cardService.findDueCardsByUser(user);
    }

    @ApiOperation(value = "카드 due date 설정하기")
    @ApiImplicitParam(name = "setCardDueDate", value = "Card", required = true,  paramType = "json")
    @PostMapping("/{id}/date")
    @ResponseStatus(HttpStatus.CREATED)
    public Card setCardDueDate(@LoginUser User user, @PathVariable Long id, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.setCardDueDate(user, id, cardDetailDto);
    }

    @ApiOperation(value = "카드 due date 변경하기")
    @ApiImplicitParam(name = "updateCardDate", value = "Card", required = true,  paramType = "json")
    @PutMapping("/{id}/date")
    public Card updateCardDate(@LoginUser User user, @PathVariable Long id, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.updateCardDate(user, id, cardDetailDto);
    }

    @ApiOperation(value = "카드 due date 삭제하기")
    @ApiImplicitParam(name = "deleteCardDate", value = "Card", required = true,  paramType = "json")
    @DeleteMapping("/{id}/date")
    public Card deleteCardDate(@PathVariable Long id) {
        return cardService.deleteCardDate(id);
    }

    @ApiOperation(value = "카드 라벨 설정하기")
    @ApiImplicitParam(name = "setCardLabel", value = "Card", required = true,  paramType = "json")
    @PostMapping("/details/label/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Card setCardLabel(@LoginUser User user, @PathVariable Long id, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.setCardLabel(user, id, cardDetailDto);
    }

    @ApiOperation(value = "카드 설명 변경하기")
    @ApiImplicitParam(name = "updateCardDescription", value = "Card", required = true,  paramType = "json")
    @PostMapping("/{cardId}/description")
    public Card updateCardDescription(@LoginUser User user, @PathVariable Long cardId, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.updateCardDescription(user, cardId, cardDetailDto);
    }

    @ApiOperation(value = "카드 이름 변경하기")
    @ApiImplicitParam(name = "updateCardTitle", value = "CardDetailDto", required = true,  paramType = "json")
    @PutMapping("/{cardId}/title")
    public CardDetailDto updateCardTitle(@LoginUser User user, @PathVariable Long cardId, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.updateCardTitle(user, cardId, cardDetailDto);
    }

    @ApiOperation(value = "카드에 assignee 할당하기")
    @ApiImplicitParam(name = "assignCardToUser", value = "AssigneeDto 리스트", required = true,  paramType = "json")
    @PostMapping("/{cardId}/assign")
    public List<AssigneeDto> assignCardToUser(@LoginUser User user, @PathVariable Long cardId, @RequestBody CardDetailDto cardDetail) {
        return cardService.assignCardToUser(user, cardId, cardDetail);
    }

    @ApiOperation(value = "카드에 assignee 제거하기")
    @ApiImplicitParam(name = "dischargeCardFromUser", value = "AssigneeDto 리스트", required = true,  paramType = "json")
    @DeleteMapping("/{cardId}/assign")
    public List<AssigneeDto> dischargeCardFromUser(@PathVariable Long cardId, @RequestBody CardDetailDto cardDetail) {
        return cardService.dischargeCardFromUser(cardId, cardDetail);
    }

    @ApiOperation(value = "카드에 댓글 달기")
    @ApiImplicitParam(name = "addComment", value = "Comment", required = true,  paramType = "json")
    @PostMapping("/{cardId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@LoginUser User user, @PathVariable Long cardId, @Valid @RequestBody CommentDto dto) {
        return cardService.addComment(user, cardId, dto);
    }

    @ApiOperation(value = "카드 댓글 제거하기")
    @ApiImplicitParam(name = "removeComment", value = "Comment", required = true,  paramType = "json")
    @DeleteMapping("/{cardId}/comments/{commentId}")
    public Comment removeComment(@LoginUser User user, @PathVariable Long cardId, @PathVariable Long commentId) {
        return cardService.removeComment(user, cardId, commentId);
    }

    @ApiOperation(value = "카드 댓글 리스트 가져오기")
    @ApiImplicitParam(name = "getComments", value = "Comment 리스트", required = true,  paramType = "json")
    @GetMapping("/{cardId}/comments")
    public List<Comment> getComments() {
        return cardService.getComments();
    }

    @ApiOperation(value = "카드 가져오기")
    @ApiImplicitParam(name = "getCard", value = "CardDetailDto", required = true,  paramType = "json")
    @GetMapping("/{cardId}")
    public CardDetailDto getCard(@PathVariable Long cardId) {
        return cardService.getCardDetail(cardId);
    }

    @ApiOperation(value = "카드의 라벨 가져오기")
    @ApiImplicitParam(name = "getLabels", value = "Label 리스트", required = true,  paramType = "json")
    @GetMapping("/{cardId}/label")
    @ResponseStatus(HttpStatus.OK)
    public List<Label> getLabels(@PathVariable Long cardId) {
        return cardService.getLabels(cardId);
    }

    @ApiOperation(value = "카드의 라벨 추가하기")
    @ApiImplicitParam(name = "addLabel", value = "Label 리스트", required = true,  paramType = "json")
    @PostMapping("/{cardId}/label")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Label> addLabel(@LoginUser User user, @PathVariable Long cardId, @RequestBody Label label) {
        return cardService.addLabel(user, cardId, label);
    }

    @ApiOperation(value = "카드의 라벨 제거하기")
    @ApiImplicitParam(name = "deleteLabel", value = "Label 리스트", required = true,  paramType = "json")
    @DeleteMapping("/{cardId}/label")
    public List<Label> deleteLabel(@PathVariable Long cardId, @RequestBody Label label) {
        return cardService.deleteLabel(cardId, label.getId());
    }

    @ApiOperation(value = "카드 멤버 조회해서 가져오기")
    @ApiImplicitParam(name = "getMembers", value = "AssigneeDto 리스트", required = true,  paramType = "json")
    @GetMapping("/{cardId}/members")
    public List<AssigneeDto> getMembers(@PathVariable Long cardId, @RequestParam String keyword) {
        return cardService.getMembers(cardId, keyword);
    }

    @ApiOperation(value = "카드 파일 첨부하기")
    @ApiImplicitParam(name = "addFile", value = "Attachment 리스트", required = true,  paramType = "json")
    @PostMapping("/{cardId}/file")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Attachment> addFile(@PathVariable Long cardId, @RequestPart MultipartFile file) {
        return cardService.addFile(cardId, file);
    }

    @ApiOperation(value = "카드 파일 제거하기")
    @ApiImplicitParam(name = "deleteFile", value = "Attachment 리스트", required = true,  paramType = "json")
    @DeleteMapping("/{cardId}/file/{fileId}")
    public List<Attachment> deleteFile(@PathVariable Long cardId, @PathVariable Long fileId) {
        return cardService.deleteFile(cardId, fileId);
    }
}
