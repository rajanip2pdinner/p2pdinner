//
//  TitleTableViewCell.m
//  P2PDinner
//
//  Created by Selvam M on 3/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "DescriptionTableViewCell.h"
#import "ItemDetailsShared.h"
#import "StringConstants.h"

@implementation DescriptionTableViewCell
- (IBAction)editButtonAction:(id)sender {
    UIButton *button=(UIButton *)sender;
    if ([button.titleLabel.text isEqualToString:kEdit]) {
        [DescrTextView becomeFirstResponder];
        [self.editButton setTitle:kDone forState:UIControlStateNormal];
    }else if ([button.titleLabel.text isEqualToString:kDone]){
        [DescrTextView resignFirstResponder];
        [self.editButton setTitle:kEdit forState:UIControlStateNormal];
    }
 
}

-(void)initialSetUpCell{
 
    [DescrTextView setText:[[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] dinnerDescription]];
}
- (BOOL)textViewShouldBeginEditing:(UITextView *)textView{
    [self.editButton setTitle:kDone forState:UIControlStateNormal];
    textView.layer.cornerRadius=2.0f;
    textView.layer.masksToBounds=YES;
    textView.layer.borderColor=[[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:0.5]CGColor];
    textView.layer.borderWidth= 0.5f;
    return TRUE;
}
- (BOOL)textViewShouldEndEditing:(UITextView *)textView{
    [self.editButton setTitle:kEdit forState:UIControlStateNormal];

    textView.layer.borderColor=[[UIColor clearColor]CGColor];
    //[textField resignFirstResponder];
return TRUE;
}
@end
