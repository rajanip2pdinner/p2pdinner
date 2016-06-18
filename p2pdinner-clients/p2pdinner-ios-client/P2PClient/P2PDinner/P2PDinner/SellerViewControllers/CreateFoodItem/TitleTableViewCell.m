//
//  TitleTableViewCell.m
//  P2PDinner
//
//  Created by Selvam M on 3/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "TitleTableViewCell.h"

@implementation TitleTableViewCell
- (void)initialSetUpCell{
    ItemDetails *sharedItem=[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue];
    [titleTextField setText:sharedItem.title];
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    // Prevent crashing undo bug – see note below.
    if(range.length + range.location > textField.text.length)
    {
        return NO;
    }
    
    NSUInteger newLength = [textField.text length] + [string length] - range.length;
    if (newLength > 25) {
        [self textFieldChangeborderColor:textField];
    }
    else{
        titleTextField.textColor=[UIColor blackColor];
    }
    return (newLength > 25) ? NO : YES;
}
- (void)textFieldChangeborderColor:(UITextField *)textField{
    
    [UIView animateWithDuration:.25
                     animations:^{
                         
                         titleTextField.textColor=[UIColor redColor];
                         
                     }
                     completion:^(BOOL finished){
                     }];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [_itemDetails setTitle:textField.text];
    textField.layer.borderColor=[[UIColor clearColor]CGColor];
    [textField resignFirstResponder];
    return true;
}
- (void)textFieldDidEndEditing:(UITextField *)textField{
    [_itemDetails setTitle:textField.text];
    textField.layer.borderColor=[[UIColor clearColor]CGColor];
    [textField resignFirstResponder];
}
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    textField.layer.cornerRadius=2.0f;
    textField.layer.masksToBounds=YES;
    textField.layer.borderColor=[[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:0.5]CGColor];
    textField.layer.borderWidth= 0.5f;
    
    return true;
}
@end
