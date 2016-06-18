//
//  TitleTableViewCell.h
//  P2PDinner
//
//  Created by Selvam M on 3/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "ItemDetails.h"
#import "ItemDetailsShared.h"


@interface TitleTableViewCell : UITableViewCell
{
    IBOutlet UITextField *titleTextField;

}
@property(nonatomic,strong)ItemDetails *itemDetails;

- (void)initialSetUpCell;
@end
