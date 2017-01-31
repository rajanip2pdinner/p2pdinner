//
//  SelectPhotoCell.m
//  P2PDinner
//
//  Created by Selvam M on 3/30/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "SelectPhotoCell.h"
#import "ItemDetailsShared.h"
#import "StringConstants.h"

@implementation SelectPhotoCell


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}
- (IBAction)addPhotoAction:(id)sender{
    [self.delegate selectPhotoAction:sender];
}
- (IBAction)removePhotoFromCell:(id)sender{
    UIButton *btn=(UIButton *)sender;
    [self.delegate removePhotoOf:self atTag:btn.tag];
    UIButton *removeTitle=(btn.tag==1)?addPhoto1:addPhoto2;
    UIImage *img;
        img=[UIImage imageNamed:kAddPhotoImageName];
    [removeTitle setBackgroundImage:img forState:UIControlStateNormal];
    [btn setHidden:YES];
    [removeTitle setTitle:kEmpty_String forState:UIControlStateNormal];
    [btn setBackgroundColor:[UIColor clearColor]];
}

@end
