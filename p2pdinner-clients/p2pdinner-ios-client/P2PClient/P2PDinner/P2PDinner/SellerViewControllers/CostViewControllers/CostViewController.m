//
//  CostViewController.m
//  P2PDinner
//
//  Created by Selvam M on 4/23/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "CostViewController.h"
#import "StringConstants.h"

@interface CostViewController()<CostCellDelegate,MinimumOrdersDelegate>
@end
@implementation CostViewController
@synthesize itemDetails;

-(void) dismissKeyboard:(id)sender
{
    [textfield1 resignFirstResponder];
    [textfield2 resignFirstResponder];
    [self updatedItems];
}
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UITapGestureRecognizer* tapBackground = [[UITapGestureRecognizer alloc] initWithTarget:self
                                                                                    action:@selector(dismissKeyboard:)];
    [tapBackground setNumberOfTapsRequired:1];
    [tableView addGestureRecognizer:tapBackground];
    
    //tableView.sectionHeaderHeight=100;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 58)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 30, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:kFont_Name size:18]];
    
    NSString *string =kCost;
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 58.0f;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 2;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)
indexPath
{
    static NSString *simpleTableIdentifier= kCostCell;
    static NSString *simpleTableIdentifier1= kMaximumCell;
    UITableViewCell *cell;
    
    if (indexPath.row==0) {
        costCell=(CostCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        costCell.delegate=self;
        [costCell setPrice:itemDetails.costPerItem];
        costCell.selectionStyle =UITableViewCellSelectionStyleNone;
        textfield1=costCell.pricePerMealTextField;
        
        return costCell;
    }
    else if (indexPath.row==1){
        minOrdCell=(MinimumOrdersCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier1];
        minOrdCell.delegate=self;
        [minOrdCell setMaximuOrdersValue:itemDetails.availableQuantity];
        textfield2=minOrdCell.maximumTextField;
        minOrdCell.selectionStyle =UITableViewCellSelectionStyleNone;
        return minOrdCell;
    }
    
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    
    
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    // This will create a "invisible" footer
    return 0.01f;
}
-(void)updatedItems{

    itemDetails.costPerItem=[costCell getCostForDinner];
    itemDetails.availableQuantity=[minOrdCell getMaximuOrdersValue];
}
@end
