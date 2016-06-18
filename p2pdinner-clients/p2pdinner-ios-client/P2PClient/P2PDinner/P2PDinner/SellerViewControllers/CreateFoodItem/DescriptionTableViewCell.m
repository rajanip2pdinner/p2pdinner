//
//  TitleTableViewCell.m
//  P2PDinner
//
//  Created by Selvam M on 3/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "DescriptionTableViewCell.h"
#import "ItemDetailsShared.h"
@implementation DescriptionTableViewCell
-(void)initialSetUpCell{
 
    [DescrTextView setText:[[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] dinnerDescription]];
}
- (BOOL)textViewShouldBeginEditing:(UITextView *)textView{
    textView.layer.cornerRadius=2.0f;
    textView.layer.masksToBounds=YES;
    textView.layer.borderColor=[[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:0.5]CGColor];
    textView.layer.borderWidth= 0.5f;
    return TRUE;
}
- (BOOL)textViewShouldEndEditing:(UITextView *)textView{
    textView.layer.borderColor=[[UIColor clearColor]CGColor];
    //[textField resignFirstResponder];
return TRUE;
}
@end
