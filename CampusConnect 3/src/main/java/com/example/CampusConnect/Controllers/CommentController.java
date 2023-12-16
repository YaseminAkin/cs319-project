package com.example.CampusConnect.Controllers;

import com.example.CampusConnect.DTO.CommentDTO;
import com.example.CampusConnect.Entities.CCuser;
import com.example.CampusConnect.Entities.Comment;
import com.example.CampusConnect.Entities.Item;
import com.example.CampusConnect.Services.CommentService;
import com.example.CampusConnect.Services.ItemService;
import com.example.CampusConnect.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService ccuserService;

    @PostMapping("/post/{itemId}/{userId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long itemId,
                                                 @PathVariable Long userId,
                                                 @RequestBody CommentDTO commentDTO) {
        // Fetch the item and user based on the provided IDs
        Item item = itemService.getItemById(itemId);
        CCuser user = ccuserService.findById(userId);

        // Create and populate a new Comment entity
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        comment.setItem(item);
        comment.setUser(user);

        // Save the comment and convert the saved entity back to DTO
        CommentDTO savedCommentDTO = CommentDTO.fromEntity(commentService.saveComment(comment));

        return new ResponseEntity<>(savedCommentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/comments/{productId}")
    public List<CommentDTO> getCommentsByProduct(@PathVariable Long productId) {
        // Fetch comments by productId
        List<CommentDTO> commentDTOs = commentService.getCommentsByProductId(productId);

        return ResponseEntity.ok(commentDTOs).getBody();
    }
}