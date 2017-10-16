//
//  CostViewController.h
//  P2PDinner
//
//  Created by Selvam M on 4/23/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CostCell.h"
#import "MinimumOrdersCell.h"
#import "ItemDetails.h"
#import "ItemDetailsShared.h"
@interface CostViewController : UIViewController
{
    UITextField *textfield1;
    UITextField *textfield2;
    
    CostCell          *costCell;
    MinimumOrdersCell *minOrdCell;
}

@property(nonatomic,strong) ItemDetails *itemDetails;

@end
